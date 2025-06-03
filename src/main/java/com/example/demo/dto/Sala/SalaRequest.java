package com.example.demo.dto.Sala;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalaRequest {
    private String name;
    private String datosJson;
}
