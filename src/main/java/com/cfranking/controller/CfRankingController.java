package com.cfranking.controller;

import com.cfranking.cache.CacheStore;
import com.cfranking.dto.Standings;
import com.cfranking.entity.CfContest;
import com.cfranking.model.CfContestList;
import com.cfranking.parser.CfParser;
import com.cfranking.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class CfRankingController {

    public static final String FINISHED = "FINISHED";

    private final CfParser cfParser;
    private final ContestRepository contestRepository;

    @Autowired
    public CfRankingController(CfParser cfParser, ContestRepository contestRepository) {
        this.cfParser = cfParser;
        this.contestRepository = contestRepository;
    }

    @RequestMapping("/standings/{contestId}")
    @CrossOrigin
    public Standings fetchStandings(@PathVariable("contestId") int contestId,
                                    @RequestParam(value = "country", defaultValue = "all") String country) {
        return cfParser.getStandings(contestId, country);
    }

    @RequestMapping("/contests")
    @CrossOrigin
    public CfContestList fetchContestList() {

        List<CfContest> cfContests = CacheStore.getCfContests();
        if (cfContests.isEmpty()) {
            contestRepository.findAll().forEach(cfContest -> {
                if (cfContest.getPhase().equals(FINISHED)) {
                    cfContests.add(cfContest);
                }
            });
            CacheStore.setCfContests(cfContests);
        }

        Collections.reverse(cfContests);
        return new CfContestList(cfContests);
    }

    @RequestMapping("/refresh-contests")
    @CrossOrigin
    public CfContestList refreshContestList() {

        contestRepository.saveAll(cfParser.getContestList());

        return new CfContestList();
    }
}
