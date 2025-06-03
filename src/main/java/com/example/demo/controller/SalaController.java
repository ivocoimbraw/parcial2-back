package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Sala.SalaBasicProjection;
import com.example.demo.dto.Sala.SalaDTO;
import com.example.demo.dto.Sala.SalaProjection;
import com.example.demo.dto.Sala.SalaRequest;
import com.example.demo.dto.Sala.SalaUpdateDTO;
import com.example.demo.service.SalaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Sala")
@RequestMapping("/sala")
@SecurityRequirement(name = "bearer-key")
public class SalaController {

    @Autowired
    private SalaService salaService;

    @PostMapping
    @Operation(
        summary = "Crea una sala",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos para crear una sala",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SalaRequest.class)
        )
    )
    )
    public ResponseEntity<SalaDTO> createSala(@RequestBody SalaDTO salaDTO) {
        System.out.println("Creando sala: " + salaDTO);
        return ResponseEntity.ok(salaService.createSalaDTO(salaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaDTO> updateSala(@RequestBody SalaUpdateDTO salaUpdateDTO, @PathVariable Integer id) {
        return ResponseEntity.ok(salaService.updateSalaDTO(salaUpdateDTO));
    }

    @GetMapping
    public ResponseEntity<List<SalaBasicProjection>> findAllSalas() {
        return ResponseEntity.ok(salaService.findAllSalas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaProjection> findSalaById(@PathVariable Integer id) {
        return ResponseEntity.ok(salaService.findSalabyId(id));
    }

    @PostMapping("/invite")
    public ResponseEntity<SalaProjection> inviteUser(@RequestParam Integer id, @RequestParam String username) {
        return ResponseEntity.ok(salaService.inviteUser(id, username));
    }

}
