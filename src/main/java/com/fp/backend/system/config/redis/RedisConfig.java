package com.fp.backend.system.config.redis;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.data.redis.host_bid}")
    private String redisHost;
    @Value("${spring.data.redis.port_bid}")
    private int redisPort;
    @Value("${spring.data.redis.host_Token}")
    private String redisHost_Token;
    @Value("${spring.data.redis.port_Token}")
    private int redisPort_Token;

//    @Bean
//    public StringRedisTemplate stringRedisTemplate() {
//        RedisConnectionFactory factory = new LettuceConnectionFactory(redisHost_Token, redisPort_Token);
//        return new StringRedisTemplate(factory);
//    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory_Bid(){
        return new LettuceConnectionFactory(redisHost, redisPort);
    }@Bean
    public RedisConnectionFactory redisConnectionFactory_Token(){
        return new LettuceConnectionFactory(redisHost_Token, redisPort_Token);
    }
    @Bean
    public RedisTemplate<?, ?> redisTemplate(){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory_Bid());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory_Bid());

        return redisTemplate;
    }
    @Bean
    public RedisTemplate<?, ?> redisTemplate_Token(){
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory_Token());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory_Token());

        return redisTemplate;
    }
}
