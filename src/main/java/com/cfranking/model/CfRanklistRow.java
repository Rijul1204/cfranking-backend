package com.cfranking.model;

import lombok.Data;

import java.util.List;

@Data
public class CfRanklistRow {

    CfParty party;
    int rank;
    String points;
    String penalty;
    List<CfProblem> problemResults;
}
