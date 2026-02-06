package com.example.blaybus4th;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Blaybus4thApplication {

	public static void main(String[] args) {
		SpringApplication.run(Blaybus4thApplication.class, args);
	}

}
