package com.example.demo.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
                                    throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null) {
            try {
                String email = jwtService.getSubject(token);

                boolean noAuthYet = SecurityContextHolder.getContext().getAuthentication() == null;
                if (email != null && noAuthYet) {
                    User user = userRepository.findByEmail(email)
                                              .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    if (jwtService.validateToken(token, user)) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                        UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        auth.setDetails(request);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (JwtException ex) {
                log.warn("Autenticación JWT fallida: {}", ex.getMessage());
                // Opcional: podrías enviar un 401 aquí y retornar en lugar de continuar
            }
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
