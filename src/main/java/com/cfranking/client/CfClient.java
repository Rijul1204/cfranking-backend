package com.cfranking.client;

import com.cfranking.model.CfContestList;
import com.cfranking.model.CfRanklistResponse;
import com.cfranking.model.CfRanklistResponseWrapper;
import com.cfranking.model.CfResponseWrapper;
import com.cfranking.model.CfUserInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class CfClient {

    private final static String RANKLIST_FETCH_URL = "https://codeforces.com/api/contest.standings";
    private final static String CONTEST_FETCH_URL = "http://codeforces.com/api/contest.list";
    private final static String USER_INFO_FETCH_URL = "http://codeforces.com/api/user.info?handles=";

    @Autowired
    RestTemplate restTemplate;

    public CfRanklistResponseWrapper getContestResults(int contestId) {
        return getContestResults(contestId, false);
    }

    public CfRanklistResponseWrapper getContestResults(int contestId, boolean showUnofficial) {
        return restTemplate.getForObject(RANKLIST_FETCH_URL + "?contestId=" + contestId
                + "&showUnofficial=" + showUnofficial, CfRanklistResponseWrapper.class);
    }

    public CfContestList getContestList() {
        return restTemplate.getForObject(CONTEST_FETCH_URL, CfContestList.class);
    }

    public List<CfUserInfo> getUserInfo(List<String> handles) {

        StringBuilder stringBuilder = new StringBuilder(USER_INFO_FETCH_URL);
        boolean first = true;
        for (String handle : handles) {
            if (!first) {
                stringBuilder.append(";");
            }
            stringBuilder.append(handle);
            first = false;
        }
        JsonNode cfResponse = restTemplate.getForObject(stringBuilder.toString(),
                JsonNode.class);
        ObjectMapper mapper = new ObjectMapper();
        CfResponseWrapper<CfUserInfo> response = mapper.convertValue(cfResponse,
                new TypeReference<CfResponseWrapper<CfUserInfo>>() {
                });
        return response.getResult();
    }
}
