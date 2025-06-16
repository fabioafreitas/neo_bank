package com.example.backend_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class BackendSpringApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendSpringApplication.class, args);
	}

}
