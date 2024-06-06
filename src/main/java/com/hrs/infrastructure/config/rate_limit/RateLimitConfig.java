package com.hrs.infrastructure.config.rate_limit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

//@Configuration
public class RateLimitConfig {
//    @Bean
    public Bucket rateLimiter() {
        Bandwidth limit = Bandwidth.classic(1000, Refill.greedy(1000, Duration.ofHours(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }
}
