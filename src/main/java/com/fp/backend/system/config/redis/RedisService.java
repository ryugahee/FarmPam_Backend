package com.fp.backend.system.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {


    @Qualifier("redisTemplate_Token")
    private final RedisTemplate<String, Object> redisTemplate_Token;


    //계정 관련
    //레디스에 엑세스토큰 저장
    public void accessTokenSave(String accessToken, String username) {

        redisTemplate_Token.opsForValue().set(username, accessToken);

//        return new ResponseEntity<>(HttpStatus.OK);
    }

    //레디스에서 동일한 엑세스토큰이 있는지 조회
    public boolean accessTokenFind(String accessToken, String username) {
        String redisAccessToken = (String) redisTemplate_Token.opsForValue().get(username);

        return redisAccessToken.equals(accessToken);

    }

    //레디스에서 엑세스토큰 삭제
    public void accessTokenDelete(String username) {

        redisTemplate_Token.delete(username);

    }

    //sms인증번호 저장
    public void smsCodeSave(String smsCode, String phoneNumber) {

        redisTemplate_Token.opsForValue().set(phoneNumber, smsCode);
    }


    //sms인증번호 일치 비교
    public boolean compareSMS(String userSMSCode, String phoneNumber) {

        return redisTemplate_Token.opsForValue().get(phoneNumber).equals(userSMSCode);
    }



}
