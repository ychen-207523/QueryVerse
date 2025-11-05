package com.queryverse.backend.config;

import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> tpl = new RedisTemplate<>();
        tpl.setConnectionFactory(cf);
        var str = new StringRedisSerializer();
        tpl.setKeySerializer(str);
        tpl.setValueSerializer(str);
        tpl.setHashKeySerializer(str);
        tpl.setHashValueSerializer(str);
        tpl.afterPropertiesSet();
        return tpl;
    }
}
