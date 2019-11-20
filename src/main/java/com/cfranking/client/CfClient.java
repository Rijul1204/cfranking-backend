package com.cfranking.client;

import com.cfranking.model.CfContest;
import com.cfranking.model.CfRanklistResponse;
import com.cfranking.model.CfRanklistResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CfClient {

    private final static String RANKLIST_FETCH_URL = "https://codeforces.com/api/contest.standings";

    private final static String CONTEST_FETCH_URL = "http://codeforces.com/api/contest.list";
    @Autowired
    RestTemplate restTemplate;

    public CfRanklistResponse getContestResults(int contestId) {
        return getContestResults(contestId, false);
    }

    public CfRanklistResponse getContestResults(int contestId, boolean showUnofficial) {
        return restTemplate.getForObject(RANKLIST_FETCH_URL + "?contestId=" + contestId
                + "&showUnofficial=" + showUnofficial, CfRanklistResponseWrapper.class).getResult();
    }

    public CfContest getContestList() {
        return restTemplate.getForObject(CONTEST_FETCH_URL, CfContest.class);
    }
}
