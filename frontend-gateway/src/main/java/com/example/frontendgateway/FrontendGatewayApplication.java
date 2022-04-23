package com.example.frontendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FrontendGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendGatewayApplication.class, args);
    }

}
