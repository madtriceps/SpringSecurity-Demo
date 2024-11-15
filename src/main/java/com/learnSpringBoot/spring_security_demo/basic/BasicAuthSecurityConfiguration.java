package com.learnSpringBoot.spring_security_demo.basic;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BasicAuthSecurityConfiguration {
	@Bean
	SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				(requests) ->{
					requests
//					.requestMatchers("/users").hasRole("USER")
//					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().authenticated();
				
				});
//		http.formLogin(withDefaults());
		http.sessionManagement(
				session -> 
					session.sessionCreationPolicy(
							SessionCreationPolicy.STATELESS
							))
		.httpBasic();
		http.csrf((csrf) -> csrf.disable()) ;
		http.headers().frameOptions().sameOrigin();
		return http.build();

	}
	
//	@Bean
//	public UserDetailsService userDetailService() {
//		var user = User.withUsername("Maddy")
//				.password("{noop}dummy")
//				.roles("USER")
//				.build();
//		
//		var admin = User.withUsername("admin")
//				.password("{noop}dummy")
//				.roles("ADMIN")
//				.build();
//		
//		return new InMemoryUserDetailsManager(user,admin);
//		}
	
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
				.build();
		}
	
	@Bean
	public UserDetailsService userDetailService(DataSource dataSource) {
		var user = User.withUsername("Maddy")
				//.password("{noop}dummy")
				.password("dummy")
				.passwordEncoder(str -> passwordEncoder().encode(str))
				.roles("USER")
				.build();
		
		var admin = User.withUsername("admin")
				//.password("{noop}dummy")
				.password("dummy")
			    .passwordEncoder(str -> passwordEncoder().encode(str))
				.roles("ADMIN")
				.build();
		
		var superAdmin = User.withUsername("super")
				//.password("{noop}dummy")
				.password("dummy")
			    .passwordEncoder(str -> passwordEncoder().encode(str))
				.roles("ADMIN","USER")
				.build();
		
		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.createUser(user);
		jdbcUserDetailsManager.createUser(admin);
		jdbcUserDetailsManager.createUser(superAdmin);
		return jdbcUserDetailsManager;
		
//		return new InMemoryUserDetailsManager(user,admin);
		}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
		
}

