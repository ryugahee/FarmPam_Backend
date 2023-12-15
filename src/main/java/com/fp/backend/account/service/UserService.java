package com.fp.backend.account.service;

import com.fp.backend.account.common.AuthorityName;

import com.fp.backend.account.dto.LoginDto;
import com.fp.backend.account.dto.SignupDto;
import com.fp.backend.account.entity.Authorities;
import com.fp.backend.account.entity.Users;
import com.fp.backend.account.enums.HeaderOptionName;
import com.fp.backend.account.repository.AuthoritiesRepository;
import com.fp.backend.account.repository.UserRepository;
import com.fp.backend.account.sms.SmsUtil;
import com.fp.backend.system.config.redis.RedisService;

import com.fp.backend.system.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final AuthoritiesRepository authoritiesRepository;
    private final RedisService redisService;
    private final SmsUtil smsUtil;

    //회원가입
    public ResponseEntity<String> userSignUp(SignupDto dto) {

        Optional<Users> dbUser = userRepository.findByUsername(dto.getUsername());

        if (dbUser.isEmpty()) {

            Users user = Users.builder()
                    .username(dto.getUsername())
                    .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .nickname(dto.getNickname())
                    .enabled(true)
                    .build();

            Authorities authorities = Authorities.builder()
                    .username(dto.getUsername())
                    .authority(AuthorityName.USER.getKey())
                    .build();

            authoritiesRepository.save(authorities);

            userRepository.save(user);


            return ResponseEntity.ok().body("가입 성공");

        }
        return ResponseEntity.badRequest().body("가입 실패");
    }


    //로그인
    public ResponseEntity<Map<String, String>> userLogin(LoginDto dto, HttpServletResponse response) throws IOException {

        System.out.println("유저 login service: " + dto.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        System.out.println("로그인 토큰 확인 : " + authenticationToken);

        Map<String, String> map = new HashMap<>();

        List<Authorities> userRoles = authoritiesRepository.findByUsername(dto.getUsername());

        Optional<Users> user = userRepository.findByUsername(dto.getUsername());
//
        System.out.println("유저 : " + user.toString());
//
        System.out.println("비밀번호 비교" + bCryptPasswordEncoder.encode(dto.getPassword()));


        if (userRoles.isEmpty()) { // role이 없다는 건 회원가입한 유저가 아니라는 뜻, 입력한 패스워드도 일치하지 않으면
            System.out.println("가입한 유저가 아니라서 리턴시키기");
            map.put("errMsg", "아이디 또는 비밀번호를 잘못 입력했습니다.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }


        boolean isAdmin = false;

        boolean isGuest = false;

        for (Authorities authority : userRoles) {

            if (AuthorityName.ADMIN.getKey().equals(authority.getAuthority())) {
                isAdmin = true;
                break;
            }
            if (AuthorityName.GUEST.getKey().equals(authority.getAuthority())) {
                isGuest = true;
                break;
            }
        }

        String userRole = "USER_ROLE";

        if (isGuest) {
            System.out.println("사용자가 ROLE_GUEST를 가지고 있습니다.");
            authoritiesRepository.updateAuthorityByUsername(dto.getUsername(), AuthorityName.USER.getKey());

        }

        String redirectUri = "";

        if (isAdmin) {
            System.out.println("사용자가 ROLE_ADMIN을 가지고 있습니다.");

            redirectUri = "admin"; // 관리자 페이지로

            userRole = AuthorityName.ADMIN.getKey();
        } else {
            redirectUri = "home"; // 홈으로
        }

        String accessToken = tokenProvider.createAccessToken();

        // RedisService에서 로그인 할 때 생성한 엑세스 토큰 저장하기
        redisService.accessTokenSave(accessToken, dto.getUsername());

        String refreshToken = tokenProvider.createRefreshToken();


        //로그인 응답에 보낼 권한명, 엑세스, 리프레시 토큰


        map.put(HeaderOptionName.ROLE.getKey(), userRole);
        map.put(HeaderOptionName.ACCESSTOKEN.getKey(), accessToken);
        map.put(HeaderOptionName.REFRESHTOKEN.getKey(), refreshToken);
        map.put("username", dto.getUsername());
        map.put("redirectPage", redirectUri);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);


//        System.out.println("authentication 확인 : " + authentication);
//
//        System.out.println("인증됐는지 확인 : " + authentication.isAuthenticated());

        if (!authentication.isAuthenticated()) {
            System.out.println("로그인 실패");
            return null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

//        return null;

        return new ResponseEntity<>(map, HttpStatus.OK);

    }

    //로그아웃
    public void userLogout(HttpServletResponse response) {

        String username = response.getHeader("username");

        System.out.println("로그아웃 요청하는 유저네임 : " + username);

        //DB(Redis)에서 엑세스 토큰 삭제하는 로직
        redisService.accessTokenDelete(username);

    }

    //간편 로그인 유저 추가 정보 입력
    public void addtionalRegister(SignupDto dto, String accessToken) {

      String username = redisService.findUsernameByAccessToken(accessToken);

        System.out.println("유저네임 확인 : " + username);

        //추가 정보를 입력했으니, ROLE을 GUEST에서 USER로 교체
        authoritiesRepository.updateAuthorityByUsername(username, AuthorityName.USER.getKey());

       Optional<Users> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
          Users updateUser =  Users.builder()
                  .username(user.get().getUsername())
                  .password(user.get().getPassword())
                  .realName(dto.getRealName())
//                  .nickname(user.get().getNickname())
                  .phoneNumber(dto.getPhoneNumber())
                  .age(dto.getAge())
                  .mailCode(dto.getMailCode())
                  .streetAddress(dto.getStreetAddress())
                  .detailAddress(dto.getDetailAddress())
                .build();

            userRepository.save(updateUser);
        }
    }


    //휴대폰 인증번호 전송
    public void userPhoneCheck(String phoneNumber) {
        Random random = new Random();

        // 1000부터 9999까지의 범위에서 랜덤 숫자 생성
        int minRange = 1000;
        int maxRange = 9999;
        int randomNumber = random.nextInt(maxRange - minRange + 1) + minRange;

        redisService.smsCodeSave(Integer.toString(randomNumber), phoneNumber);

//        smsUtil.sendOne(phoneNumber, randomNumber);
    }

    //인증번호 확인 비교
    public String compareSMSCode(String userSMSCode, String phoneNumber) {

        //인증번호가 일치하지 않으면
        if (!redisService.compareSMS(userSMSCode, phoneNumber)) {
            return "인증번호를 다시 입력해주세요";
        }

        return "";

    }


    //모든 유저 가져오기
    public List<Users> getAllUser() {

       List<Users> allUsers = userRepository.findAll();

        return allUsers;
    }



}
