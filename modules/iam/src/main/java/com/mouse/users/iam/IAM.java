package com.mouse.users.iam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mouse.users.iam")
public class IAM {
    public static void main(String[] args) {
        SpringApplication.run(IAM.class);
    }
}
