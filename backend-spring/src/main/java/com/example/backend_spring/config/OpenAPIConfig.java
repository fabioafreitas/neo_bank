package com.example.backend_spring.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
		info = @Info(
				title = "NeoBank API",
				version = "1.0",
				description = "API para operações bancárias e marketplace",
				contact = @Contact(name = "Fábio Alves", email = "fabio.alves.frei@gmail.com")
		)
)
@Configuration
public class OpenAPIConfig {
}

