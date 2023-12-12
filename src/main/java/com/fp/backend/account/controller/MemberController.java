package com.fp.backend.account.controller;

import com.fp.backend.account.common.AuthorityName;

import com.fp.backend.account.dto.LoginDto;
import com.fp.backend.account.dto.SignupDto;
import com.fp.backend.account.dto.TokenDto;
import com.fp.backend.account.entity.Users;
import com.fp.backend.account.enums.HeaderOptionName;

import com.fp.backend.account.service.UserService;
import com.fp.backend.account.util.SmsUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final SmsUtil smsUtil;

    //RDBMS
    private final UserService userService;

    //NoSQL
    private final RedisUserService redisUsersService;


    @PostMapping("/user/signup")
    public void signup(@RequestBody SignupDto dto) {

        System.out.println("회원가입 요청: " + dto.getUsername());

        userService.userSignUp(dto);

    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto dto, HttpServletResponse response) throws IOException {

        System.out.println("로그인 요청 컨트롤러 진입: " + dto.getUsername() + ",  " + dto.getPassword());

//        return redisUsersService.userLogin(dto, response);

        return userService.userLogin(dto, response);

//        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/userLogout")
    public void logout(HttpServletResponse response) {
        System.out.println("로그아웃 요청");

        userService.userLogout(response);

    }

    @GetMapping("/test")
    public String test() {
        System.out.println("필터 다 거치고 테스트로");
        return "인가 완료";
    }

    @PostMapping("/googleLogin")
    public void googleLogin() {

        System.out.println("구글구글구글구글구글구글구글구글 로그인 요청");
    }

    @PostMapping("/requireRefreshToken")
    public ResponseEntity refresh(HttpServletResponse response) {

        System.out.println("응답 토큰 확인 : " + response.getHeader("accessToken"));

        Map<String, String> map = new HashMap<>();

        map.put(HeaderOptionName.ROLE.getKey(), AuthorityName.USER.getKey());
        map.put(HeaderOptionName.ACCESSTOKEN.getKey(), response.getHeader("accessToken"));
        map.put(HeaderOptionName.REFRESHTOKEN.getKey(), response.getHeader("refreshToken"));

        System.out.println("리프레시 컨트롤러");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/checkPhoneNumber")
    public String phoneCheck(@RequestBody String phoneNumber) {

        System.out.println("휴대폰 번호 인증 컨트롤러 작동");

        userService.userPhoneCheck(phoneNumber);

        return "";

    }

    @PostMapping("/compareSMSNumber")
    public ResponseEntity smsNumberCheck(@RequestBody String smsNumber, @RequestBody String phoneNumber) {

        String result = userService.compareSMSCode(smsNumber, phoneNumber);

        if (result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.OK);

        }

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }
  

}