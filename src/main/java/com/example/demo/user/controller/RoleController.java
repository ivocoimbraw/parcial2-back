package com.example.demo.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.entity.Role;
import com.example.demo.user.service.RoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/role")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Role", description = "Role")
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public void createRole(Role role) {
        roleService.createRole(role);
    }

    @GetMapping("/get")
    public Iterable<Role> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/get/{id}")
    public Role getRoleById(Long id) {
        return roleService.getRoleById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRoleById(Long id) {
        roleService.deleteRoleById(id);
    }

}
