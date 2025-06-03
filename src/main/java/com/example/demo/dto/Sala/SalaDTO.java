package com.example.demo.dto.Sala;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalaDTO {
    private Integer id;
    private String name;
    private UserDTO owner;
    private String datosJson;
    private List<UserDTO> users;
}
