package com.fp.backend.controller;

import com.fp.backend.dto.LoginDto;
import com.fp.backend.dto.SignupDto;
import com.fp.backend.dto.TokenDto;
import com.fp.backend.entity.Member;
import com.fp.backend.jwt.TokenProvider;
import com.fp.backend.service.UserService;
import com.fp.backend.system.util.UUIDProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final UserService userService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UUIDProvider uuidProvider;

    @PostMapping("/user/signup")
    public ResponseEntity<Member> signup(@RequestBody SignupDto dto) {

        System.out.println("회원가입 요청: " + dto.getUsername());

        return ResponseEntity.ok(userService.save(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto dto , HttpServletResponse response) {

        System.out.println("로그인 요청 컨트롤러 진입: " + dto);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication); // jwt 토큰 생성
        String uuid = uuidProvider.createUUID();
        // JWT 토큰을 쿠키에 설정
        Cookie cookie = new Cookie("jwtToken", jwt); // "jwtToken" 쿠키 이름
        cookie.setHttpOnly(true); // HTTPOnly 플래그 설정
//        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 설정 (예: 1일)
        cookie.setPath("/"); // 쿠키의 경로 설정 (루트 경로로 설정)

        response.addCookie(cookie); // 응답 헤더에 쿠키 추가

        return new ResponseEntity<>(HttpStatus.OK);
    }


}