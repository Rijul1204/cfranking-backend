package com.cfranking.cache;

import com.cfranking.entity.CfContest;
import com.cfranking.entity.CfUserInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.ArrayList;
import java.util.List;

public class CacheStore {

    private static List<CfContest> cfContests = new ArrayList<>();
    public static Cache<String, CfUserInfo> cache = CacheBuilder.newBuilder().maximumSize(300000000).build();

    public static List<CfContest> getCfContests() {
        return cfContests;
    }

    public static void setCfContests(List<CfContest> cfContests) {
        CacheStore.cfContests = cfContests;
    }
}
