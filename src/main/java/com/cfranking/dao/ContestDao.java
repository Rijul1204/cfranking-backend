package com.cfranking.dao;

import com.cfranking.model.CfContest;
import com.cfranking.service.ConfigPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ContestDao {

    private static final String CONTESTS_KEY = "CONTESTS";

    @Autowired
    private ListOperations<String, CfContest> listOperations;

    @Autowired
    private RedisTemplate<String, Object> template;

    @Autowired
    private ConfigPropertyService configPropertyService;

    public void persist(List<CfContest> cfContests) {
        listOperations.leftPushAll(CONTESTS_KEY, cfContests);

        int expire = configPropertyService.getContextListExpireInSecond();

        template.expire(CONTESTS_KEY, expire, TimeUnit.SECONDS);
    }

    public List<CfContest> getContestList() {
        return listOperations.range(CONTESTS_KEY, 0, -1);
    }
}
