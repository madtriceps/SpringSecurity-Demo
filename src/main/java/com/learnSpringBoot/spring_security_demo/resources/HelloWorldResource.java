package com.learnSpringBoot.spring_security_demo.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {
	
	@GetMapping("/hello-world")
	public String helloWorld() {
		return "Hello - World people, we're working on Spring Security v2!";
	}
	

}
