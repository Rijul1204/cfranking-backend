package com.cfranking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CfrankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CfrankingApplication.class, args);
    }
}
