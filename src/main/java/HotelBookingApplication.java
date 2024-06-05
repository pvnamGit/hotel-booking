import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.hrs")
@EntityScan("com.hrs.core.domain")
@EnableCaching()
public class HotelBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelBookingApplication.class, args);
    }
}
