package com.example.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The main class to start the admin module.
 */
@SpringBootApplication(scanBasePackages = {"com.example.library.*", "com.example.admin.*"})
@EnableJpaRepositories(value = "com.example.library.repositories")
@EntityScan(value = "com.example.library.models")
public class AdminApplication {
  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
  }
}
