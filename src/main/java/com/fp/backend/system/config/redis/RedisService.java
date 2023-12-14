package com.fp.backend.system.config.redis;

import com.fp.backend.system.config.websocket.SocketVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;


import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {
//
//    @Qualifier("stringRedisTemplate")
//    private final StringRedisTemplate stringRedisTemplate;

    @Qualifier("redisTemplate_Token")
    private final RedisTemplate<String, Object> redisTemplate_Token;


    public void setValues(String key, String data){
        ValueOperations<String, Object> values = redisTemplate_Token.opsForValue();
        values.set(key, data);
    }
    public void setValues(String key, String data, Duration duration){
        ValueOperations<String, Object> values = redisTemplate_Token.opsForValue();
        values.set(key, data, duration);
    }
    @Transactional(readOnly = true)
    public String getValues(String key){
        ValueOperations<String, Object> values = redisTemplate_Token.opsForValue();
        if(values.get(key) == null){
            return"false";
        }
        return (String) values.get(key);
    }
    public void setValuesPush(String key, String data){
        ListOperations<String, Object> list = redisTemplate_Token.opsForList();

        list.leftPush(key, data);
        System.out.println("data = " + data);
    }
    public String getValuesLastIndex(String key){
        ListOperations<String, Object> list = redisTemplate_Token.opsForList();
        return (String) list.index(key, 0);
    }
    @Transactional(readOnly = true)
    public List<SocketVO> getValuesListAll(String key){
        ListOperations<String,Object> list = redisTemplate_Token.opsForList();
        List<SocketVO> stringList = new ArrayList<>();
        List<Object> bidList = list.range(key, 0, -1);
        for(int i = bidList.size()-1; i >= 0; i--){
            Object bid = list.index(key, i);
            SocketVO socketVO = new SocketVO(key, bid);
            stringList.add(socketVO);
        }

        return stringList;
    }


    public void deleteValues(String key){
        redisTemplate_Token.delete(key);
    }
    public void expireValues(String key, int timeout){
        redisTemplate_Token.expire(key, timeout, TimeUnit.MILLISECONDS);
    }


    public void setHashOps(String key, Map<String, String> data) {
        HashOperations<String, Object, Object> values = redisTemplate_Token.opsForHash();
        values.putAll(key, data);
    }

    @Transactional(readOnly = true)
    public String getHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate_Token.opsForHash();
        return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ? (String) redisTemplate_Token.opsForHash().get(key, hashKey) : "";
    }

    public void deleteHashOps(String key, String hashKey) {
        HashOperations<String, Object, Object> values = redisTemplate_Token.opsForHash();
        values.delete(key, hashKey);
    }

    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }



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
