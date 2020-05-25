package com.cfranking.scheduler;

import com.cfranking.cache.CacheStore;
import com.cfranking.client.CfClient;
import com.cfranking.entity.CfContest;
import com.cfranking.entity.CfUserInfo;
import com.cfranking.model.CfRanklistResponse;
import com.cfranking.parser.CfParser;
import com.cfranking.repository.ContestRepository;
import com.cfranking.repository.PersonsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CfParserScheduler {

    private static final Logger log = LoggerFactory.getLogger(CfParserScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final int CHUNK_SIZE = 100;

    private final CfParser cfParser;
    private final ContestRepository contestRepository;
    private final CfClient cfClient;
    private final PersonsRepository personsRepository;

    @Autowired
    public CfParserScheduler(CfParser cfParser, ContestRepository contestRepository, CfClient cfClient,
                             PersonsRepository personsRepository) {
        this.cfParser = cfParser;
        this.contestRepository = contestRepository;
        this.cfClient = cfClient;
        this.personsRepository = personsRepository;
    }

    int contestIdx = 1149;

    @Scheduled(fixedRate = 10000)
    public void refreshContestList() {
        List<CfContest> contestList = cfParser.getContestList();
        contestRepository.saveAll(contestList);
    }

    @Scheduled(fixedRate = 25000)
    public void parseStandings() {


        CfContest nextContest = getNextContest();
        log.info("STARTED ---- syncing at {} ", dateFormat.format(new Date()));
        log.info("Contest Information {} ", nextContest);
        CfRanklistResponse contestResults = cfClient.getContestResults(nextContest.getId(), false).getResult();

        final AtomicInteger counter = new AtomicInteger();
        Collection<List<String>> missingHandles = contestResults.getRows().stream()
                .flatMap(row -> row.getParty().getMembers().stream())
                .map(member -> member.getHandle())
                .collect(Collectors.toList())
                .stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / CHUNK_SIZE))
                .values();

        for (List<String> list : missingHandles) {
            try {
                log.info("retrieving user info " + list.size());
                retrieveUserInfo(list);
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("FINISHED ---- syncing at {} ", dateFormat.format(new Date()));
    }

    private void retrieveUserInfo(List<String> list) throws Exception {

        List<CfUserInfo> userList = cfClient.getUserInfo(list)
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
    }

    public CfContest getNextContest() {

        List<CfContest> cfContests = CacheStore.getCfContests();
        if (cfContests.isEmpty()) {
            for (CfContest cfContest : contestRepository.findAll()) {
                cfContests.add(cfContest);
            }
            CacheStore.setCfContests(cfContests);
        }

        if (contestIdx >= cfContests.size()) {
            contestIdx = 0;
        }
        return cfContests.get(contestIdx++);
    }
}
