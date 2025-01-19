package com.hrs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HotelBookingApplication {
  private static final Logger logger = LoggerFactory.getLogger(HotelBookingApplication.class);

  public static void main(String[] args) {
    logger.info("Start the application...");
    SpringApplication.run(HotelBookingApplication.class, args);
    logger.info("Application started...");
  }
}
