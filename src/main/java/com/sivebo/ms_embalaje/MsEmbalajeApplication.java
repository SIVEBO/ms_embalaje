package com.sivebo.ms_embalaje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsEmbalajeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEmbalajeApplication.class, args);
    }
}