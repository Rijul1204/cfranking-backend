package com.cfranking.model;

import com.cfranking.entity.CfContest;
import lombok.Data;

import java.util.List;

@Data
public class CfRanklistResponse {

    private CfContest contest;
    private List<CfProblem> problems;
    private List<CfRanklistRow> rows;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CfRanklistResponse{");
        sb.append("contest=").append(contest);
        sb.append(", problems=").append(problems);
        sb.append(", rows=").append(rows);
        sb.append('}');
        return sb.toString();
    }
}
