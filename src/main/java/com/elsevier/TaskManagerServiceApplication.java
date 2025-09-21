package com.elsevier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerServiceApplication {

  /**
   * Application entry point — boots the Spring Boot TaskManagerService application.
   *
   * <p>The JVM entry method invoked to start the application; delegates to SpringApplication.run,
   * forwarding any command-line arguments.
   *
   * @param args command-line arguments passed through to SpringApplication
   */
  public static void main(String[] args) {
    SpringApplication.run(TaskManagerServiceApplication.class, args);
  }
}
