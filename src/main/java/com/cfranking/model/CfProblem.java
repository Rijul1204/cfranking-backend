package com.cfranking.model;

import lombok.Data;

@Data
public class CfProblem {

    String index;
    String name;
    int points;
    int rejectedAttemptCount;
    int penalty;
    String type;
}
