package com.cfranking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis-expire")
@Data
public class RedisCacheEvictionProperties {
    private int contestListExpireInSecond;
}