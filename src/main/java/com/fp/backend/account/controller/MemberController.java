package com.fp.backend.account.controller;

import com.fp.backend.account.common.AuthorityName;
import com.fp.backend.account.dto.LoginDto;
import com.fp.backend.account.dto.SignupDto;
import com.fp.backend.account.dto.UserInfoDto;
import com.fp.backend.account.dto.UserDto;

import com.fp.backend.account.entity.Users;
import com.fp.backend.account.enums.HeaderOptionName;
import com.fp.backend.account.service.UserService;
import com.fp.backend.account.sms.SmsUtil;
import com.fp.backend.auction.dto.SMSVerificationDTO;
import com.fp.backend.system.config.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final SmsUtil smsUtil;

    //RDBMS
    private final UserService userService;

    //NoSQL
    private final RedisService redisUsersService;


    //회원가입
    @PostMapping("/user/signup")
    public void signup(@RequestBody SignupDto dto) {

        System.out.println("회원가입 요청: " + dto.getUsername());

        userService.userSignUp(dto);

    }


    //로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto, HttpServletResponse response) throws IOException {

        System.out.println("로그인 요청 컨트롤러 진입: " + dto.getUsername() + ",  " + dto.getPassword());

//        return redisUsersService.userLogin(dto, response);

        return userService.userLogin(dto, response);

//        return new ResponseEntity(HttpStatus.OK);
    }


    //로그아웃
    @PostMapping("/userLogout")
    public void logout(HttpServletRequest request) {
        System.out.println("로그아웃 요청");

        userService.userLogout(request);

    }

    @GetMapping("/test")
    public void test() {
        System.out.println("토큰 검증 통과!");
    }

    @PostMapping("/googleLogin")
    public void googleLogin() {

        System.out.println("구글구글구글구글구글구글구글구글 로그인 요청");
    }

    //엑세스 토큰이 유효하지 않을 때 리프레시 토큰 요청
    @PostMapping("/requireRefreshToken")
    public ResponseEntity<?> refresh(HttpServletResponse response) {

        System.out.println("응답 토큰 확인 : " + response.getHeader("accessToken"));

        Map<String, String> map = new HashMap<>();

        map.put(HeaderOptionName.ROLE.getKey(), AuthorityName.USER.getKey());
        map.put(HeaderOptionName.ACCESSTOKEN.getKey(), response.getHeader("accessToken"));
        map.put(HeaderOptionName.REFRESHTOKEN.getKey(), response.getHeader("refreshToken"));

        System.out.println("리프레시 컨트롤러 진입. 엑세스/리프레시 토큰 재발급 완료!");

        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    //간편 로그인 유저 추가 정보 입력
    @PostMapping("/additionalInfo")
    public ResponseEntity<?> additionalRegister(@RequestBody SignupDto dto, HttpServletRequest request) {

        System.out.println("간편 로그인 유저 추가 정보 입력 컨트롤러 진입");

        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");

        return userService.addtionalRegister(dto, accessToken);

//        return new ResponseEntity<>(HttpStatus.OK);

    }

    //휴대폰 인증 번호 요청
    @PostMapping("/checkPhoneNumber")
    public String phoneCheck(@RequestBody String phoneNumber, HttpServletRequest request) {

        System.out.println("휴대폰 인증번호 발송 컨트롤러 작동 : " + phoneNumber);

        phoneNumber = phoneNumber.replace("\"", "");

        String usernameBefore = request.getHeader("username");

        byte[] decodedBytes = Base64.getDecoder().decode(usernameBefore);
        String username = new String(decodedBytes, StandardCharsets.UTF_8);

        userService.userPhoneCheck(phoneNumber, username);

        return "";

    }

    //인증 번호 확인
    @PostMapping("/compareSMSNumber")
    public ResponseEntity<?> smsNumberCheck(@RequestBody SMSVerificationDTO dto) {

        System.out.println("문자 인증 번호 비교 smsNumberCheck 컨트롤러 진입");

        String result = userService.compareSMSCode(dto.getSmsNumber(), dto.getPhoneNumber());

        if (result.isEmpty()) {
            return new ResponseEntity<>(result, HttpStatus.OK);

        }

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);

    }

    //멤버 현황
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllMember(@RequestParam int pageNum) {

        System.out.println("멤버 현황 컨트롤러 : " + pageNum);

        Page<Users> allUsers = userService.getAllUser(pageNum);

        System.out.println(allUsers.getTotalPages());

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }



    //유저 정보 불러오기
    @PostMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {

        System.out.println("유저 정보 불러오기 컨트롤러 진입");

        String usernameBefore = request.getHeader("username");

        byte[] decodedBytes = Base64.getDecoder().decode(usernameBefore);
        String username = new String(decodedBytes, StandardCharsets.UTF_8);

       Users user = userService.getUserInfo(username);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //유저 정보 수정
    @PostMapping("/updateUserInfo")
    public ResponseEntity<?> updateUserInfo(
            @Valid UserInfoDto dto,
          HttpServletRequest request,
            @RequestPart(value = "file", required = false) MultipartFile imgFile
    ) throws Exception {

        System.out.println("유저 수정 컨트롤러 진입");
        System.out.println("들어온 유저 수정 데이터 확인: " + dto.toString());

//        if (imgFile != null) {
            System.out.println("이미지 파일 정보: " + imgFile.getOriginalFilename());
            // 이미지 파일이 전송된 경우 처리할 내용
//        }
        userService.updateUserInfo(dto, request, imgFile);


        return new ResponseEntity<>(HttpStatus.OK);

    }


    //아이디 중복 체크
    @PostMapping("/checkUsername")
    public ResponseEntity<?> checkId(@RequestBody String username) {

        username = username.replace("\"", "");

        System.out.println("유저네임 : " + username);

        return userService.checkId(username);

    }

    //닉네임 중복 체크
    @PostMapping("/checkNickname")
    public ResponseEntity<?> checkNickname(@RequestBody String nickname) {

        nickname = nickname.replace("\"", "");

        System.out.println("유저네임 : " + nickname);

        return userService.checkNickname(nickname);

    }


    //아이디 찾기
    @PostMapping("/findUsername")
    public ResponseEntity<?> findUsername(@RequestBody String phoneNumber) {

       return userService.findUsername(phoneNumber);

    }



    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {
        return new ResponseEntity<>(userService.getUser(username), HttpStatus.OK);
    }

}