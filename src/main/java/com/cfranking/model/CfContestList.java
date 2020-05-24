package com.cfranking.model;

import com.cfranking.entity.CfContest;
import lombok.Data;

import java.util.List;

@Data
public class CfContestList {

    private String status;
    private List<CfContest> result;

    public CfContestList() {
    }

    public CfContestList(List<CfContest> cfContests) {
        this.result = cfContests;
    }
}
