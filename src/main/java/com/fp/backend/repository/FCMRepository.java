package com.fp.backend.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fp.backend.dto.FCMRequestDto;
import com.fp.backend.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FCMRepository {
    private final StringRedisTemplate tokenRedisTemplate;

    public void saveToken(Member member){
        tokenRedisTemplate.opsForValue()
                .set(member.getEmail(), member.getAuthorities());

    }
}
