package com.cfranking.service;

import com.cfranking.config.RedisCacheEvictionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigPropertyService {

    @Autowired
    private RedisCacheEvictionProperties cacheEvictionProperties;

    public int getContextListExpireInSecond() {
        return cacheEvictionProperties.getContestListExpireInSecond();
    }
}
