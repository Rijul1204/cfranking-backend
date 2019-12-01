package com.cfranking.controller;

import com.cfranking.dao.ContestDao;
import com.cfranking.dto.Standings;
import com.cfranking.model.CfContest;
import com.cfranking.model.CfContestList;
import com.cfranking.parser.CfParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CfRankingController {

    private Logger logger = LoggerFactory.getLogger(CfRankingController.class);

    @Autowired
    private CfParser cfParser;

    @Autowired
    private ContestDao contestDao;

    @RequestMapping("/standings/{contestId}")
    public Standings fetchStandings(@PathVariable("contestId") int contestId,
                                    @RequestParam(value = "country", defaultValue = "all") String country) {
        return cfParser.getStandings(contestId);
    }

    @RequestMapping("/contests")
    public CfContestList fetchStandings() {
        List<CfContest> cfContests = contestDao.getContestList();

        if (!CollectionUtils.isEmpty(cfContests)) {
            logger.debug("Cache Hit for CfContest List");
            return new CfContestList(cfContests);
        }

        logger.debug("Cache Miss for CfContest List");

        cfContests = cfParser.getContestList();

        contestDao.persist(cfContests);

        return new CfContestList(cfContests);
    }
}
