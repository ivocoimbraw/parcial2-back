package com.example.demo.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
    
    @Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/**")
			.allowedOriginPatterns("*")
			.allowedMethods("POST", "GET", "PUT", "DELETE")
			.allowedHeaders("*")
			.exposedHeaders("header1", "header2")
			.maxAge(3600);
	}

}
