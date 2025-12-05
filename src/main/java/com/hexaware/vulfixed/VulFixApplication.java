package com.hexaware.vulfixed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hexaware.vulfixed")
public class VulFixApplication {
  public static void main(String[] args) {
    SpringApplication.run(VulFixApplication.class, args);
    System.out.println("VulFixApplication started successfully.");
  }
}