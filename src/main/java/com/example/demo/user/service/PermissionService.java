package com.example.demo.user.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.user.entity.Permission;
import com.example.demo.user.enums.Permissions;
import com.example.demo.user.repository.PermissionRepository;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    // method to create a permission
    public void createPermission(Permission permission) {
        permissionRepository.save(permission);
    }

    // method to obtain all permissions
    public Iterable<Permission> getPermissions() {
        return permissionRepository.findAll();
    }

    // method to obtain a permission by id
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    // method to delete a permission by id
    public void deletePermissionById(Long id) {
        permissionRepository.deleteById(id);
    }

    public Set<Permission> getOrCreatePermissions() {
        Set<String> defaultPermissions = Permissions.getAllPermissionNames();
        Set<Permission> existingPermissions = permissionRepository.findAllByNameIn(defaultPermissions);

        if (existingPermissions.size() < defaultPermissions.size()) {
            Set<String> existingNames = existingPermissions.stream()
                    .map(Permission::getName)
                    .collect(Collectors.toSet());

            Set<Permission> newPermissions = defaultPermissions.stream()
                    .filter(name -> !existingNames.contains(name))
                    .map(Permission::new)
                    .collect(Collectors.toSet());

            if (!newPermissions.isEmpty()) {
                Set<Permission> savedPermissions = new HashSet<>(permissionRepository.saveAll(newPermissions));
                existingPermissions.addAll(savedPermissions);
            }
        }

        return existingPermissions;
    }

}
