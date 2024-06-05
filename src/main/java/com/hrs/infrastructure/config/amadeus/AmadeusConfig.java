package com.hrs.infrastructure.config.amadeus;

import com.amadeus.Amadeus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmadeusConfig {
    @Bean
    public Amadeus amadeusBuilder() {
        Amadeus amadeus = Amadeus
                .builder("XBMgBlolGqk6GX0s04fNNZEEAbafDrZZ", "GVt8eOC7eXG0rwFT")
                .build();
        return amadeus;
    }
}
