package com.example.demo.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.entity.Permission;
import com.example.demo.user.service.PermissionService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/permission")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Permission", description = "Permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/get")
    public Iterable<Permission> getPermissions() {
        return permissionService.getPermissions();
    }

    @GetMapping("/get/{id}")
    public Permission getPermissionById(Long id) {
        return permissionService.getPermissionById(id);
    }

}
