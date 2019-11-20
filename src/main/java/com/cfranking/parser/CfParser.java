package com.cfranking.parser;

import com.cfranking.client.CfClient;
import com.cfranking.dto.ContestMeta;
import com.cfranking.dto.Problem;
import com.cfranking.dto.RankRow;
import com.cfranking.dto.Standings;
import com.cfranking.model.CfContest;
import com.cfranking.model.CfRanklistResponse;
import com.cfranking.model.CfRanklistRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CfParser {

    @Autowired
    CfClient cfClient;

    Map<String, String> handleToCountryMap;

    public Standings getStandings(int contestId) {
        CfRanklistResponse contestResults = cfClient.getContestResults(contestId, false);
        return convertToStandings(contestResults);
    }

    // TODO : Refactor to new bean
    private Standings convertToStandings(CfRanklistResponse contestResults) {
        Standings standings = new Standings();
        standings.setContestMeta(getContestMeta(contestResults));
        standings.setRows(getRows(contestResults.getRows()));
        return standings;
    }

    private List<RankRow> getRows(List<CfRanklistRow> rows) {

        return rows.stream()
                .map(row -> {
                    RankRow rankRow = new RankRow();
                    rankRow.setHandle(row.getParty().getMembers().get(0).getHandle());
                    rankRow.setRank(row.getRank());
                    rankRow.setPoints(row.getPoints());
                    return rankRow;
                })
                .collect(Collectors.toList());
    }

    private ContestMeta getContestMeta(CfRanklistResponse contestResults) {

        ContestMeta contestMeta = new ContestMeta();
        CfContest contest = contestResults.getContest();

        contestMeta.setId(contest.getId());
        contestMeta.setName(contest.getName());
        contestMeta.setPhase(contest.getPhase());

        contestMeta.setProblemList(getProblemList(contestResults));
        return contestMeta;
    }

    private List<Problem> getProblemList(CfRanklistResponse contestResults) {

        return contestResults.getProblems().stream()
                .map(cfProblem -> {
                    Problem problem = new Problem();
                    problem.setIndex(cfProblem.getIndex());
                    problem.setName(cfProblem.getName());
                    problem.setPoints(cfProblem.getPoints());
                    return problem;
                }).collect(Collectors.toList());
    }
}
