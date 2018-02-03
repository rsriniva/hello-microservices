package com.redhat.training.msa.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

   public static void main(String[] args) {
     LOG.info("Starting Spring Boot application...");
      ApplicationContext ctx = SpringApplication.run(Application.class, args);
   }
}
