package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.Sala.SalaDTO;
import com.example.demo.dto.Sala.UserDTO;
import com.example.demo.model.Sala;
import com.example.demo.repository.SalaRepository;
import com.example.demo.service.SalaService;
import com.example.demo.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @InjectMocks
    private SalaService salaService;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private UserRepository userRepository;

     @Test
    void createSalaDTO_CreaSalaYLaGuarda() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        UserDTO ownerDTO = UserDTO.builder()
                .id(ownerId)
                .email("user@example.com")
                .build();

        SalaDTO salaDTO = SalaDTO.builder()
                .owner(ownerDTO)
                .datosJson("{\"clave\":\"valor\"}")
                .build();

        // Act
        SalaDTO resultado = salaService.createSalaDTO(salaDTO);

        // Assert
        assertNotNull(resultado);
        verify(salaRepository).save(any(Sala.class));
    }
}
