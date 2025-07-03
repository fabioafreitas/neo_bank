package com.example.backend_spring.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {
	@GetMapping("/")
	public String redirectToSwagger() {
		return "redirect:/swagger-ui.html";
	}
}
