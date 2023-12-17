package com.fp.backend.system.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class TokenRedisConfig extends RedisConfig{
    @Bean
    public RedisConnectionFactory TokenRedisConnectionFactory() {
        return redisConnectionFactory(2);
    }

    @Bean(name = "redisTemplate_Token")
    @Qualifier("redisTemplate_Token")
    public RedisTemplate<?, ?> redisTemplate_Token(){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(TokenRedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
