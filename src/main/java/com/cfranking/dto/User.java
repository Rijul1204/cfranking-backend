package com.cfranking.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    String handle;
    String country;
    String organization;
    String rating;
}
