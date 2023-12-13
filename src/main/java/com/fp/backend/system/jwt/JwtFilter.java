package com.fp.backend.system.jwt;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.enums.ApiName;
import com.fp.backend.account.enums.HeaderOptionName;
import com.fp.backend.account.enums.MessageName;
import com.fp.backend.system.config.redis.RedisService;
import com.fp.backend.system.util.PasswordUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private final RedisService redisUserService;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    public JwtFilter(TokenProvider tokenProvider, RedisService redisUserService) {
        this.tokenProvider = tokenProvider;
        this.redisUserService = redisUserService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        //헤더에서 유저네임 꺼내기
        String username = request.getHeader("username");



        System.out.println("JwtFilter 작동");

        System.out.println("들어온 주소 확인 : " + request.getRequestURI());

        Users user = Users.builder()
                .username(username)
                .password(PasswordUtil.generateRandomPassword())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, null,
                        authoritiesMapper.mapAuthorities(user.getAuthorities()));

        if (
                request.getRequestURI().equals(ApiName.LOGIN.getKey()) ||
                        request.getRequestURI().equals(ApiName.SIGNUP.getKey()) ||
                        request.getRequestURI().equals(ApiName.LOGOUT.getKey()) ||
                        request.getRequestURI().equals("/api/checkPhoneNumber") ||
                        request.getRequestURI().equals("/favicon.ico")
//                        ||
//                        request.getRequestURI().equals("/api/item/allMarketValues") ||
//                        request.getRequestURI().equals("/api/item/marketValue") ||
//                        request.getRequestURI().equals("/api/item/new") ||
//                        request.getRequestURI().equals("/api/item/getAuctionOngoing")


        ) {
            System.out.println("로그인/회원가입/로그아웃 요청이라 JwtFilter 통과!");
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }


        //엑세스 토큰 추출
        String accessToken = tokenProvider
                .extractAccessToken(request)
                .orElse(null);

        System.out.println("엑세스 토큰 : " + accessToken);

        //헤더에 엑세스 토큰이 있다면
        if (accessToken != null) {
            //액세스 토큰에 대한 판단
            ResponseEntity accessTokenResponseEntity = decideAccessTokenPath(accessToken);

            //TODO: 중복 로그인 검증. 레디스에서 해당 유저 네임의 엑세스 토큰이 일치하는지 확인
            boolean result = redisUserService.accessTokenFind(accessToken, request.getHeader("username"));


            System.out.println("중복 로그인 검증 결과 : " + result);

            System.out.println("엑세스 토큰 판단 결과 : " + accessTokenResponseEntity);

            if (accessTokenResponseEntity != null) {
                // accessTokenResponseEntity null이 아니라면, 즉 액세스 토큰이 유효하지 않을 경우
                response.setStatus(accessTokenResponseEntity.getStatusCodeValue()); // 상태 코드 설정
                response.getWriter().write(accessTokenResponseEntity.getBody().toString()); // 응답 메시지 설정
                return;
            }

            if (!result) {

                System.out.println("로그아웃 처리를(중복로그인이라) 해야 하므로 엑세스 토큰 삭제");
                //로그아웃 처리를 - 중복로그인이라 해야 하므로 엑세스 토큰 삭제
                redisUserService.accessTokenDelete(request.getHeader("username"));
//                new ResponseEntity<>(MessageName.RE_LOGIN.getKey(), HttpStatus.UNAUTHORIZED);

                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                response.getWriter().write(MessageName.RE_LOGIN.getKey());

                return; // 메시지를 보낸 후에는 이후 로직을 실행하지 않고 종료
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        } // 엑세스 토큰이 없다면 아래 계속 진행


        //리프레시 토큰에 대한 판단
        ResponseEntity refreshTokenResponseEntity = decideRefreshTokenPath(request);

        if (refreshTokenResponseEntity != null) {
            //refreshTokenResponseEntity가 null이 아닌 것은 리프레시 토큰이 유효하지 않다는 뜻이라 사용자가 다시 로그인하게 해야 함
            response.setStatus(refreshTokenResponseEntity.getStatusCodeValue()); // 상태 코드 설정
            response.getWriter().write(refreshTokenResponseEntity.getBody().toString()); // 응답 메시지 설정
            return;
        }




        /*
         * refreshTokenResponseEntity가 null 이라면(판단한 메소드의 리턴값이 null 이라면) 리프레시 토큰이 유효하다는 의미이므로,
         *엑세스 토큰 재발급(덮어쓰기) 및 리프레시 토큰을 갱신해야 함
         * */

        //새 엑세스 토큰 생성
        String newAccessToken = tokenProvider.createAccessToken();

        //DB에 있던 기존 엑세스 토큰을 새걸로 덮어쓰기
        redisUserService.accessTokenSave(newAccessToken, username);

        //새 리프레시 토큰 생성(새 엑세스 토큰을 생성하면 리프레시 토큰도 새걸로 교체해야 보안상 좋음)
        String newRefreshToken = tokenProvider.createRefreshToken();

//        System.out.println("새 엑세스 토큰 : " + newAccessToken + ", 새 리프레시 토큰 : " + newRefreshToken);

        //새로 발급받은 엑세스, 리프레시 토큰 응답 헤더에 설정
        response.setHeader(HeaderOptionName.ACCESSTOKEN.getKey(), newAccessToken);
        response.setHeader(HeaderOptionName.REFRESHTOKEN.getKey(), newRefreshToken);


        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
        return;
    }

//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//
//        return null;
//    }

    public ResponseEntity decideAccessTokenPath(String accessToken) {

        System.out.println("엑세스토큰 판단 시작");

        //액세스 토큰이 유효하지 않으면 클라이언트에 401에러로 리프레시 토큰 보내달라는 에러메시지 담아서 보내기
        if (accessToken != null) {
            boolean result = tokenProvider.validateToken(accessToken);
            if (!result) {
                System.out.println("액세스 토큰 유효하지 않음");

                return new ResponseEntity<>(MessageName.REQUIRE_REFRESH_TOKEN.getKey(), HttpStatus.UNAUTHORIZED);
            }
        }
        return null;
    }

    public ResponseEntity decideRefreshTokenPath(HttpServletRequest request) {

        System.out.println("리프레시 토큰 판단 시작");

        //리프레시 토큰 추출
        String refreshToken = tokenProvider
                .extractRefreshToken(request)
                .orElse(null);

        System.out.println("리프레시 토큰 : " + refreshToken);

        //리프레시 토큰이 유효하지 않으면 클라이언트에 401에러와 다시 로그인 하라는 에러메시지 담아서 보내기
        boolean result = tokenProvider.validateToken(refreshToken);
        if (!result) {
            System.out.println("리프레시토큰 유효하지 않음");

            System.out.println("레디스에서 삭제할 키값 : " + request.getHeader("username"));

            //로그아웃 처리를 해야 하므로 엑세스 토큰 삭제
            redisUserService.accessTokenDelete(request.getHeader("username"));

            return new ResponseEntity<>(MessageName.RE_LOGIN.getKey(), HttpStatus.UNAUTHORIZED);
        }

        return null;
    }
}