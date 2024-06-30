package com.learnSpringBoot.spring_security_demo.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;
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
        new Todo("John", "Complete Spring Boot tutorial")
    );

    @GetMapping("/todos")
    public List<Todo> retrieveAllTodos() {
        return TODOS_LIST;
    }
    
    @GetMapping("/users/{username}/todos")
    public List<Todo> retrieveTodosForSpecificUser(@PathVariable("username") String username) {
        return TODOS_LIST.stream()
                         .filter(todo -> todo.username().equals(username))
                         .collect(Collectors.toList());
    }
    
    @PostMapping("/users/{username}/todos")
    public void createTodosForSpecificUser(@PathVariable("username") String username,
    		@RequestBody Todo todo) {
    	logger.info("Create {} for {}",todo,username);
    	
        return ;
    }
}

record Todo (String username, String description) {}
