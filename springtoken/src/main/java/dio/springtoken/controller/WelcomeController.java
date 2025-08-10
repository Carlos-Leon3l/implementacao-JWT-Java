package dio.springtoken.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
	@GetMapping
	public String welcome() {
		return "Welcome to My SpringBoot Web API";
	}
	
	@GetMapping("/users")
	public String users() {
		return "Authorizes user";
	}
	
	@GetMapping("/managers")
	public String managers() {
		return "Authorized manager";
	}
	
}
