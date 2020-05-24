package com.cfranking.parser;

import com.cfranking.client.CfClient;
import com.cfranking.dto.ContestMeta;
import com.cfranking.dto.Problem;
import com.cfranking.dto.RankRow;
import com.cfranking.dto.Standings;
import com.cfranking.entity.CfContest;
import com.cfranking.model.CfContestList;
import com.cfranking.model.CfRanklistResponse;
import com.cfranking.model.CfRanklistRow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CfParser {

    private final CfClient cfClient;
    private final CfUserStore userStore;

    public CfParser(CfClient cfClient, CfUserStore userStore) {
        this.cfClient = cfClient;
        this.userStore = userStore;
    }

    public List<CfContest> getContestList() {
        CfContestList cfContestList = cfClient.getContestList();
        return cfContestList.getResult();
    }

    public Standings getStandings(int contestId, String country) {

        CfRanklistResponse contestResults = cfClient.getContestResults(contestId, false).getResult();
        userStore.generateMissingHandlesInfo(contestResults);
        return convertToStandings(contestResults, country);
    }


    // TODO : Refactor to new bean
    private Standings convertToStandings(CfRanklistResponse contestResults, String country) {

        Standings standings = new Standings();
        standings.setContestMeta(getContestMeta(contestResults));
        standings.setRows(getRows(contestResults.getRows(), country));
        return standings;
    }

    private List<RankRow> getRows(List<CfRanklistRow> rows, String country) {

        return getRows(rows)
                .stream()
                .filter(rankRow ->
                        country.equals("all") ? true : country.equals(rankRow.getCountry())
                ).collect(Collectors.toList());
    }

    private List<RankRow> getRows(List<CfRanklistRow> rows) {
        final AtomicInteger counter = new AtomicInteger();
        rows.stream()
                .map(row -> {
                    return row.getParty().getMembers().get(0).getHandle();
                }).collect(Collectors.toList())
                .stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 500))
                .values()
                .forEach(list -> userStore.retrieveUserInfo(list));



        return rows.stream()
                .map(row -> {
                    RankRow rankRow = new RankRow();
                    rankRow.setHandle(row.getParty().getMembers().get(0).getHandle());
                    rankRow.setStanding(row.getRank());
                    rankRow.setPoints(row.getPoints());
                    rankRow.setCountry(userStore.getUser(rankRow.getHandle()).getCountry());
                    rankRow.setRating(userStore.getUser(rankRow.getHandle()).getRating());
                    rankRow.setUserRank(userStore.getUser(rankRow.getHandle()).getRank());
                    rankRow.setProblemResultList(row.getProblemResults());
                    return rankRow;
                }).collect(Collectors.toList());
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
