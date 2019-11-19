package com.cfranking.dto;

import lombok.Data;

import java.util.List;

@Data
public class RankRow {
    String handle;
    boolean ghost;
    long rank;
    long points;
    List<ProblemResult> problemResultList;
}
