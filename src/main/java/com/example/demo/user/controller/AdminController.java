package com.example.demo.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Admin", description = "Admin")
@PreAuthorize("hasAuthority('PERMISSION_READ')")
public class AdminController {
    
    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    public String test() {
        return "Acceso de lectura";
    }

    @GetMapping("/escritura")
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    public String test2() {
        return "Acceso a escritura";
    }

    @GetMapping("/noaccess")
    @PreAuthorize("hasAuthority('PERMISSION_NO')")
    public String test3() {
        return "No acceso";
    }


}
