package com.example.demo.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.user.dto.UserView;
import com.example.demo.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String username);
    Optional<UserView> findUserViewByEmail(String email);
    boolean existsByEmail(String superAdminEmail);
}
