package com.example.demo.dto.Sala;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private UUID id;
    private String email;
}
