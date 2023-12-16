package com.fp.backend.system.config.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class BidRedisConfig extends RedisConfig{

    @Bean
    @Primary
    public RedisConnectionFactory bidRedisConnectionFactory() {
        return redisConnectionFactory(0);
    }

    @Bean(name = "redisTemplate_Bid")
    @Qualifier("redisTemplate_Bid")
    public RedisTemplate<?, ?> redisTemplate_Bid(){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(bidRedisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

}
