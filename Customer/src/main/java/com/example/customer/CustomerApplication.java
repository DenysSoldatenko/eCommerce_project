package com.example.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The main class to start the customer module.
 */
@SpringBootApplication(scanBasePackages = {"com.example.customer.*", "com.example.library.*"})
@EnableJpaRepositories(value = "com.example.library.repositories")
@EntityScan(value = "com.example.library.models")
public class CustomerApplication {
  public static void main(String[] args) {
    SpringApplication.run(CustomerApplication.class, args);
  }
}