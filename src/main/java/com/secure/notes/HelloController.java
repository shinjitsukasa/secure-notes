package com.secure.notes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@GetMapping("/hello")
	public String hello() {
		return "Hello, World!";
	}

	@GetMapping("/secure")
	public String secure() {
		return "Secure endpoint!";
	}

	@GetMapping("/public")
	public String publicEndpoint() {
		return "Public endpoint!";
	}

	@GetMapping("/contact")
	public String contact() {
		return "Contact endpoint!";
	}

	// @GetMapping("/private")
	// public String privateEndpoint() {
	// 	return "Private endpoint!";
	// }

	// @GetMapping("/admin")
	// public String adminEndpoint() {
	// 	return "Admin endpoint!";
	// }

	// @GetMapping("/user")
	// public String userEndpoint() {
	// 	return "User endpoint!";
	// }

	// @GetMapping("/guest")
	// public String guestEndpoint() {
	// 	return "Guest endpoint!";
	// }
}
