package com.cfranking.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class User {
    String handle;
    String country;
    String organization;
    String rating;
}
