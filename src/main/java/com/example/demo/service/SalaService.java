package com.example.demo.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Sala.SalaBasicProjection;
import com.example.demo.dto.Sala.SalaDTO;
import com.example.demo.dto.Sala.SalaProjection;
import com.example.demo.dto.Sala.SalaUpdateDTO;
import com.example.demo.dto.Sala.UserDTO;
import com.example.demo.model.Sala;
import com.example.demo.repository.SalaRepository;
import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private UserRepository userRepository;

    public SalaDTO createSalaDTO(SalaDTO salaDTO) {
        User owner = getUserAuthenticated();

     

        Sala sala = new Sala();
        sala.setOwner(owner);
        sala.setName(salaDTO.getName());
        sala.setDatosJson(salaDTO.getDatosJson());
        // salaRepository.save(sala);
        Sala savedSala = salaRepository.save(sala);

         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy - 'Hrs:'HH:mm");

        SalaDTO result = SalaDTO.builder()
        .id(savedSala.getId())
        .name(savedSala.getName())    
        .datosJson(savedSala.getDatosJson())
        .users(null)  
        .createdAt(savedSala.getCreatedAt().format(formatter))
        .build();
 
        return result;
    }

    public SalaDTO updateSalaDTO(SalaUpdateDTO salaUpdateDTO) {
        Sala sala = salaRepository.findById(salaUpdateDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("No existe la sala"));
        sala.setDatosJson(salaUpdateDTO.getDatosJson());
        salaRepository.save(sala);
        return salaDTO(sala);
    }

    public List<SalaBasicProjection> findAllSalas() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        return salaRepository.findByOwner(user);
    }

    private User getUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails userDetails)) {
            throw new IllegalArgumentException("No se pudo obtener el usuario autenticado");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Revisa tu autenticación"));
        return user;
    }

    public SalaProjection findSalabyId(Integer id) {
        return salaRepository
                .findProjectedById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la sala"));
    }

    public SalaProjection inviteUser(Integer salaId, String username) {
        Sala sala = salaRepository.findByIdWithUsers(salaId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la sala"));
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario"));
        sala.getUsers().add(user);
        salaRepository.save(sala);
        return salaRepository.findProjectedById(salaId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la sala"));
    }

    private SalaDTO salaDTO(Sala sala) {
        return SalaDTO.builder()
                .id(sala.getId())
                .owner(UserDTO.builder()
                        .id(sala.getOwner().getId())
                        .email(sala.getOwner().getUsername())
                        .build())
                .datosJson(sala.getDatosJson())
                .users(sala.getUsers().stream()
                        .map(user -> UserDTO.builder()
                                .id(user.getId())
                                .email(user.getUsername())
                                .build())
                        .toList())
                .build();
    }

}
