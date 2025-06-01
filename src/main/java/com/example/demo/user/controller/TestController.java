package com.example.demo.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/test")
@Tag(name = "Test", description = "Test")
public class TestController {
    
    @GetMapping
    public String test() {
        return "Test";
    }
}
