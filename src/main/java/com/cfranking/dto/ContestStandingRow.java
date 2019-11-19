package com.cfranking.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContestStandingRow {
    User user;
    String point;
    List<Problem> problemList;
    List<ContestStandingRow> contestStandingRowList;
}
