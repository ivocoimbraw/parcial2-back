package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Ping")
public class PingController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
