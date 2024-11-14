package com.learnSpringBoot.spring_security_demo.resources;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoResource {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final List<Todo> TODOS_LIST = List.of(
        new Todo("Maddy", "Learn DSA"),
        new Todo("Maddy", "Get a dream job"),
        new Todo("John", "Complete Spring Boot tutorial"),
        new Todo("John", "Complete Spring Boot tutorial 1"),
        new Todo("John", "Complete Spring Boot tutorial 2"),
        new Todo("John", "Complete Spring Boot tutorial 3"),
        new Todo("super", "Complete Spring Boot tutorial for super user"),
        new Todo("admin", "Complete Spring Boot tutorial for admin")
    );

    @GetMapping("/todos")
    public List<Todo> retrieveAllTodos() {
        return TODOS_LIST;
    }
    
    @GetMapping("/users/{username}/todos")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PreAuthorize("hasAuthority('ROLE_USER') and (#username == null or #username.equals(authentication.name))")

    public List<Todo> retrieveTodosForSpecificUser(@PathVariable("username") String username, Authentication auth) {
    	if(auth.getName().equals(username)) {
    		System.out.println("YESS");
    		if(auth.getName().toString() == username.toString()) {
    			System.out.println("yeah...this too");
    		  }
    	}
    	else {
    		System.out.println("NOOOOOOO" + "Username:"+username+", authname:"+auth.getName());
    	}
        return TODOS_LIST.stream()
                         .filter(todo -> todo.username().equals(username))
                         .collect(Collectors.toList());
    }


    
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDenied(AccessDeniedException ex, Authentication authentication) {
        System.out.println("Access denied for user: " + authentication.getName());
        System.out.println("Attempted access to resource by username: " + ex.getMessage());
    }
    
    
    @PostMapping("/users/{username}/todos")
    public void createTodosForSpecificUser(@PathVariable("username") String username,
    		@RequestBody Todo todo) {
    	logger.info("Create {} for {}",todo,username);
    	
        return ;
    }
}

record Todo (String username, String description) {}
