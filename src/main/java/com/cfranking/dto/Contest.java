package com.cfranking.dto;

import lombok.Data;

@Data
public class Contest {
    String contestId;
    String name;
    String type;
    String phase;
    long durationSeconds;
    long startTimeSeconds;
    long relativeTimeSeconds;
}
