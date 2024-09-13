package com.fmd.jschdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JschdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JschdemoApplication.class, args);
    }

}
