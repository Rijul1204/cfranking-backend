package com.cfranking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
class RedisProperties {
    private String host;
    private int port;
    private String user;
    private String password;
}