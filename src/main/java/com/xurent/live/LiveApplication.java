package com.xurent.live;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class LiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveApplication.class, args);
    }

}
