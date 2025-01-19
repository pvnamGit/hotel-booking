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
    SpringApplication.run(HotelBookingApplication.class, args);
    logger.debug("This is a debug message");
    logger.info("This is a info message");
    logger.warn("This is a warn message");
    logger.error("This is a error message");
  }
}
