package me.dragonappear.springsecuritycustom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSecurityCustomApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityCustomApplication.class, args);
	}
}
