package com.fp.backend.system.config.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host_Bid}")
    private String redisHost;
    @Value("${spring.data.redis.port_Bid}")
    private int redisPort;
    @Value("${spring.data.redis.host_Token}")
    private String redisHost_Token;
    @Value("${spring.data.redis.port_Token}")
    private int redisPort_Token;



    @Bean(name = "redisConnectionFactory_Bid")
    public RedisConnectionFactory redisConnectionFactory_Bid(){
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    @Primary
    @Bean(name = "redisConnectionFactory_Token")
    public RedisConnectionFactory redisConnectionFactory_Token(){
        return new LettuceConnectionFactory(redisHost_Token, redisPort_Token);
    }

    @Primary
    @Bean(name = "redisTemplate_Bid")
    public RedisTemplate<?, ?> redisTemplate_Bid(@Qualifier("redisConnectionFactory_Bid")RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }


    @Bean(name = "redisTemplate_Token")
    public RedisTemplate<?, ?> redisTemplate_Token(@Qualifier("redisConnectionFactory_Token")RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

}