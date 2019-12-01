package com.cfranking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
class RedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public HashOperations hashOperations() {
        return redisTemplate().opsForHash();
    }

    @Bean
    public ListOperations listOperations() {
        return redisTemplate().opsForList();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setEnableDefaultSerializer(true);
        template.setConnectionFactory(redisConnectionFactory());
        template.afterPropertiesSet();

        return template;
    }

    private JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(),
                redisProperties.getPort());
        config.setPassword(RedisPassword.of(redisProperties.getPassword()));

        return new JedisConnectionFactory(config);
    }
}