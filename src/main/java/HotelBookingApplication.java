import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hrs")
public class HotelBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelBookingApplication.class, args);
    }
}
