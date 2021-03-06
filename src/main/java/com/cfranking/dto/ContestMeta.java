package com.cfranking.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContestMeta {
    int id;
    String name;
    String phase;
    List<Problem> problemList;
}
