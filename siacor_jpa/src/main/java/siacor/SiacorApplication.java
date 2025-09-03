package siacor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SiacorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiacorApplication.class, args);
	}

}
