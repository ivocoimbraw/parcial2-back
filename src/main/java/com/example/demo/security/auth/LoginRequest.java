package com.example.demo.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Schema(example = "admin@example.com")
    private String username;
    @Schema(example = "SuperSecurePassword123")
    private String password;
}
