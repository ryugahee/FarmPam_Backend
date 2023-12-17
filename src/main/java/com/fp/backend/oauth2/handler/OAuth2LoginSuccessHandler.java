package com.fp.backend.oauth2.handler;

import com.fp.backend.account.entity.Authorities;
import com.fp.backend.account.entity.Users;
import com.fp.backend.account.enums.AuthorityName;
import com.fp.backend.account.repository.AuthoritiesRepository;
import com.fp.backend.system.config.redis.RedisService;
import com.fp.backend.system.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    private final AuthoritiesRepository authoritiesRepository;

    private final RedisService redisUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("간편 로그인 성공!");

        try {

            Users oAuth2User = (Users) authentication.getPrincipal();

            loginSuccess(response, oAuth2User);

            //간편 로그인 후 추가 정보를 입력하지 않았다면(ROLE이 GUEST라면) 추가 정보 입력 페이지로 리다이렉트
            List<Authorities> userAuth = authoritiesRepository.findByUsername(oAuth2User.getUsername());

            System.out.println("간편로그인 유저 권한 확인 : " + userAuth.get(0).getAuthority().equals(AuthorityName.GUEST.getKey()));

            if (userAuth.get(0).getAuthority().equals(AuthorityName.GUEST.getKey())) {
                System.out.println("추가 정보 입력 페이지로");
                response.sendRedirect("http://localhost:8081/easyLogin");
            } else {

                response.sendRedirect("http://localhost:8081/login");
            }

        } catch (Exception e) {
            throw e;
        }

    }



    private void loginSuccess(HttpServletResponse response, Users oAuth2User)  throws IOException {

        List<Authorities> userAuth = authoritiesRepository.findByUsername(oAuth2User.getUsername());

        response.setHeader("accessToken", tokenProvider.createAccessToken());
        response.setHeader("refreshToken", tokenProvider.createRefreshToken());

        // TokenProvider를 사용하여 accessToken, refreshToken 등을 생성
        String accessToken = tokenProvider.createAccessToken();

        //엑세스 토큰 저장 로직
        redisUserService.accessTokenSave(accessToken, oAuth2User.getUsername());

        String refreshToken = tokenProvider.createRefreshToken();

        // 쿠키 생성
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        Cookie usernameCookie = new Cookie("username", oAuth2User.getUsername());
        Cookie userRoles = new Cookie("roles", userAuth.get(0).getAuthority());

        // 쿠키의 경로 설정
        accessTokenCookie.setPath("/");
        refreshTokenCookie.setPath("/");
        usernameCookie.setPath("/");
        userRoles.setPath("/");

        // 쿠키를 HTTP 응답 헤더에 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.addCookie(usernameCookie);
        response.addCookie(userRoles);

    }
}
