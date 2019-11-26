package com.cfranking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CfUserInfo {

    String country;
    String handle;
    String city;
    String rank;
    String organization;
}
