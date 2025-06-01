package com.example.demo.security.auth;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.user.dto.UserView;
import com.example.demo.user.entity.Role;
import com.example.demo.user.entity.User;
import com.example.demo.exception.GenericException;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.security.jwt.JwtService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository; 
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserView user = userRepository.findUserViewByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Login: Usuario no encontrado"));
        
        String token = jwtService.generateToken(user);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .user(user)
                .build();

        return authResponse;
    }

    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
        Optional<User> userExist = userRepository.findByEmail(request.getUsername()); 
        if (userExist.isPresent()) {
            throw new UsernameNotFoundException("This user already exist.");
        }
        Role role = new Role();
        role.setId((short) 2);
        User user = User.builder()
                .name(request.getNombre())
                .email(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        UserView userView = userRepository.findUserViewByEmail(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Login: Usuario no encontrado"));

        String token = jwtService.generateToken(userView);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .user(userView)
                .build();

        return authResponse;
    }

    public void updatePassword(PasswordRequest passwordRequest) {
        Optional<User> optionalPerson = userRepository.findByEmail(passwordRequest.getUsername()); 
        if (optionalPerson.isPresent()) {
            String oldPassword = optionalPerson.get().getPassword();
            String newPassword = passwordEncoder.encode(passwordRequest.getNewPassword());
            if (passwordEncoder.matches(passwordRequest.getOldPassword(), oldPassword)) {
                User updatedUser = optionalPerson.get();
                updatedUser.setPassword(newPassword);
                userRepository.save(updatedUser);
            }else {
                throw new GenericException("La contraseña actual es incorrecta.");
            }
        }else {
            throw new GenericException("Usuario no encontrado.");
        }
    }


}