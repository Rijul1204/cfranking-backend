package com.cfranking.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Standings {

    ContestMeta contestMeta = new ContestMeta();
    List<RankRow> rows = new ArrayList<>();
}
