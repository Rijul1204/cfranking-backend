package com.cfranking.parser;

import com.cfranking.cache.CacheStore;
import com.cfranking.client.CfClient;
import com.cfranking.entity.CfUserInfo;
import com.cfranking.model.CfRanklistResponse;
import com.cfranking.repository.PersonsRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CfUserStore {

    private static final long ENTRY_LIMIT = 100000000;
    private final int CHUNK_SIZE = 500;

    private final CfClient cfClient;
    private final PersonsRepository personsRepository;

    public CfUserStore(CfClient cfClient, PersonsRepository personsRepository) {
        this.cfClient = cfClient;
        this.personsRepository = personsRepository;
        personsRepository.findAll().forEach(
                (person) -> {
                    CacheStore.cache.put(person.getHandle(), person);
                }
        );
    }

    public void generateMissingHandlesInfo(CfRanklistResponse contestResults) {

        final AtomicInteger counter = new AtomicInteger();
        Collection<List<String>> missingHandles = contestResults.getRows().stream()
                .flatMap(row -> row.getParty().getMembers().stream())
                .map(member -> member.getHandle())
                .filter(handle -> CacheStore.cache.getIfPresent(handle) == null)
                .collect(Collectors.toList())
                .stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / CHUNK_SIZE))
                .values();

        missingHandles.stream().forEach(list -> retrieveUserInfo(list));
    }

    public CfUserInfo getUser(String handle) {

        CfUserInfo result = CacheStore.cache.getIfPresent(handle);
        if (result != null) {
            return result;
        }
        retrieveUserInfo(Arrays.asList(handle));
        result = CacheStore.cache.getIfPresent(handle);
        if (result != null) {
            return result;
        }
        return personsRepository.findById(handle).orElseGet(() -> new CfUserInfo());
    }

    public List<CfUserInfo> retrieveUserInfo(List<String> handles) {

        List<String> missingHandles = handles
                .stream()
                .filter(handle -> CacheStore.cache.getIfPresent(handle) == null)
                .collect(Collectors.toList());

        Map<String, CfUserInfo> userInfoFromDB = personsRepository
                .findAllById(missingHandles)
                .stream()
                .collect(Collectors.toMap((user) -> user.getHandle(), (user) -> user));

        for (CfUserInfo user : userInfoFromDB.values()) {
            if (user.getCountry() == null) {
                user.setCountry("");
            }
            CacheStore.cache.put(user.getHandle(), user);
        }

        missingHandles = missingHandles.stream().filter(handle -> !userInfoFromDB.containsKey(handle))
                .collect(Collectors.toList());

        List<CfUserInfo> userList = cfClient.getUserInfo(missingHandles)
                .stream()
                .filter(user -> user.getHandle() != null)
                .collect(Collectors.toList());

        for (CfUserInfo user : userList) {
            if (user.getCountry() == null) {
                user.setCountry("");
            }
            CacheStore.cache.put(user.getHandle(), user);
        }
        personsRepository.saveAll(userList);

        return userList;
    }

}
