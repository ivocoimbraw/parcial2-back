package com.example.demo.security.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializerRunner implements CommandLineRunner {

    @Autowired
    private UserInitializerService userInitializerService;

    @Override
    public void run(String... args) {
        userInitializerService.createSuperAdminIfNotExists();
    }
}