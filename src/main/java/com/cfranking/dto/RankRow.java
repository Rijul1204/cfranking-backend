package com.cfranking.dto;

import com.cfranking.model.CfProblem;
import lombok.Data;

import java.util.List;

@Data
public class RankRow {
    String handle;
    boolean ghost;
    long standing;
    String points;
    String country;
    String org;
    String rating;
    String userRank;
    List<CfProblem> problemResultList;
}
