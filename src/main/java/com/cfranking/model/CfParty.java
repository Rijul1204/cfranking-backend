package com.cfranking.model;

import lombok.Data;

import java.util.List;

@Data
public class CfParty {

    List<CfMember> members;
    String participantType;
    String teamName;
    boolean ghost;
}
