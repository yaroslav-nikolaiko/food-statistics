package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FoodReviewsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoodReviewsApplication.class, args);
    }
}
