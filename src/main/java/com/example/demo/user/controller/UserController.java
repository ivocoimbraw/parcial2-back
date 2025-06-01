package com.example.demo.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.dto.UserView;
import com.example.demo.user.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "User", description = "User")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserView findUserProfileDTO() {
        return userService.findUserProfileDTO();
    }

}
