package com.example.demo.security.auth;

import lombok.Data;

@Data
public class PasswordRequest {
    
    private String username;

    private String oldPassword;
    
    private String newPassword;
}
