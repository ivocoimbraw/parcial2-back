package com.example.demo.user.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Set<Permission> findAllByNameIn(Set<String> names);
}
