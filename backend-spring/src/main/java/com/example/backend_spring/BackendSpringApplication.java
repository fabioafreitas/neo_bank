package com.example.backend_spring;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BackendSpringApplication {
	public static void main(String[] args) {
		// String db = System.getenv("POSTGRES_DB");
		// String usr = System.getenv("POSTGRES_USER");
		// String psk = System.getenv("POSTGRES_PASSWORD");
		// String port = System.getenv("POSTGRES_PORT");
		// String url = System.getenv("POSTGRES_HOST");
		// Flyway flyway = Flyway.configure()
		// 	.dataSource("jdbc:postgresql://"+url+":"+port+"/"+db, usr, psk)
		// 	.cleanDisabled(false)
		// 	.load();
		// flyway.clean();
		// flyway.migrate();
		SpringApplication.run(BackendSpringApplication.class, args);
	}

}
