package com.example.demo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.user.entity.Role;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.enums.Roles;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionService permissionService;

    //method to create a role
    public void createRole(Role role) { 
        roleRepository.save(role);
    }

    //method to obtain all roles
    public Iterable<Role> getRoles() {
        return roleRepository.findAll();
    }

    //method to obtain a role by id
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    //method to delete a role by id
    public void deleteRoleById(Long id) {
        roleRepository.deleteById(id);
    }

    public Role getOrCreateAdminRole() {
        Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN.name());
        Role userRole = roleRepository.findByName(Roles.ROLE_USER.name());
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName(Roles.ROLE_ADMIN.name());
            adminRole.setPermissions(permissionService.getOrCreatePermissions());
            roleRepository.saveAndFlush(adminRole);
        }
        if (userRole == null) {
            userRole = new Role();
            userRole.setName(Roles.ROLE_USER.name());
            roleRepository.saveAndFlush(userRole);
        }
        return adminRole;
    }

}
