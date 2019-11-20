package com.cfranking.dto;

import lombok.Data;

import java.util.List;

@Data
public class RankRow {
    String handle;
    boolean ghost;
    long rank;
    String points;
    List<ProblemResult> problemResultList;
}
