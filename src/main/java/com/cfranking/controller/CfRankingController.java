package com.cfranking.controller;

import com.cfranking.dto.Standings;
import com.cfranking.model.CfContest;
import com.cfranking.parser.CfParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CfRankingController {

    @Autowired
    CfParser cfParser;

    @RequestMapping("/standings/{contestId}")
    public Standings fetchStandings(@PathVariable("contestId") int contestId,
                                    @RequestParam(value = "country", defaultValue = "all") String country) {
        return cfParser.getStandings(contestId);
    }

    @RequestMapping("/contests")
    public List<CfContest> fetchStandings() {
        return cfParser.getContestList();
    }
}
