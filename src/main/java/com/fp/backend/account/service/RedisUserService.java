package com.fp.backend.account.service;

import com.fp.backend.account.repository.AuthoritiesRepository;
import com.fp.backend.system.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUserService {

    @Qualifier("redisTemplate_Token")
    private final RedisTemplate<String, Object> redisTemplate;

    private final AuthoritiesRepository authoritiesRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

//    private final RedisAccessTokenRepository accessTokenRepository;

    public void accessTokenSave(String accessToken, String username) {

        //레디스에 엑세스토큰 저장
        redisTemplate.opsForValue().set(username, accessToken);

//        return new ResponseEntity<>(HttpStatus.OK);
    }

    public boolean accessTokenFind(String accessToken, String username) {
        //레디스에서 동일한 엑세스토큰이 있는지 조회
        String redisAccessToken = (String) redisTemplate.opsForValue().get(username);

        return redisAccessToken.equals(accessToken);

    }

    public void accessTokenDelete(String username) {

        //레디스에서 엑세스토큰 삭제
        redisTemplate.delete(username);

    }

}

