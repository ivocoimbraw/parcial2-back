package com.example.demo.security.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.service.RoleService;

import jakarta.transaction.Transactional;

@Service
public class UserInitializerService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserInitializerService.class);
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${SUPERADMIN_EMAIL}")
    private String superAdminEmail;

    @Value("${SUPERADMIN_NAME}")
    private String superAdminName;

    @Value("${SUPERADMIN_PASSWORD}")
    private String superAdminPassword;

    @Transactional
    public void createSuperAdminIfNotExists() {
        if (userRepository.existsByEmail(superAdminEmail)) {
            logger.info("✔ Usuario SuperAdmin ya existe, no se creará nuevamente.");
            return;
        }

        logger.info("🚀 Creando usuario SuperAdmin...");

        Role adminRole = roleService.getOrCreateAdminRole();

        User superAdmin = new User();
        superAdmin.setEmail(superAdminEmail);
        superAdmin.setName(superAdminName);
        superAdmin.setLastName(superAdminName);
        superAdmin.setPassword(passwordEncoder.encode(superAdminPassword));
        superAdmin.setRole(adminRole);

        userRepository.save(superAdmin);
        logger.info("✅ Usuario SuperAdmin creado exitosamente.");
    }
}