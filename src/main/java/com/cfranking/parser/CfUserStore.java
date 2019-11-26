package com.cfranking.parser;

import com.cfranking.client.CfClient;
import com.cfranking.model.CfRanklistResponse;
import com.cfranking.model.CfUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CfUserStore {

    @Autowired
    CfClient cfClient;

    private final int CHUNK_SIZE = 5000;

    Map<String, String> handleToCountryMap = new ConcurrentHashMap<>();
    Map<String, String> handleToOrgMap = new ConcurrentHashMap<>();
    Map<String, String> handleToCityMap = new ConcurrentHashMap<>();

    public void generateMissingHandlesInfo(CfRanklistResponse contestResults) {

        final AtomicInteger counter = new AtomicInteger();
        Collection<List<String>> missingHandles = contestResults.getRows().stream()
                .flatMap(row -> row.getParty().getMembers().stream())
                .map(member -> member.getHandle())
                .filter(handle -> !handleToCountryMap.containsKey(handle))
                .collect(Collectors.toList())
                .stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / CHUNK_SIZE))
                .values();

        missingHandles.parallelStream().forEach(list -> retrieveUserInfo(list));
    }

    public String getCountryName(String handle) {
        return handleToCountryMap.get(handle);
    }

    private void retrieveUserInfo(List<String> handles) {
        List<CfUserInfo> list = cfClient.getUserInfo(handles);

        for (CfUserInfo cfUserInfo : list) {
            if (cfUserInfo.getHandle() == null) {
                continue;
            }
            handleToCountryMap.put(cfUserInfo.getHandle(), String.valueOf(cfUserInfo.getCountry()));
            handleToOrgMap.put(cfUserInfo.getHandle(), String.valueOf(cfUserInfo.getOrganization()));
            handleToCityMap.put(cfUserInfo.getHandle(), String.valueOf(cfUserInfo.getCity()));
        }
    }
}
