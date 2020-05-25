package com.cfranking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CfUserInfo {

    @Id
    String handle;
    String country;
    String city;
    @Column(name = "rank_title")
    String rank;
    String organization;
    String rating;
}
