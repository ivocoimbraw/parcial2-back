package com.example.demo.interceptor;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.example.demo.model.Sala;
import com.example.demo.repository.SalaRepository;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final SalaRepository salaRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 2.1 En CONNECT extraemos y validamos el JWT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new AccessDeniedException("Falta token JWT");
            }
            String token = authHeader.substring(7);
            String username = jwtService.getSubject(token);
            // valida firma y expiración
            if (!jwtService.validateToken(token, username)) {
                throw new AccessDeniedException("Token inválido o expirado");
            }

            StompPrincipal user = new StompPrincipal(username);

            accessor.setUser(user);
            accessor.getSessionAttributes().put("user", user);
        }

        // 2.2 En SUBSCRIBE validamos que el user pueda unirse a la sala
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Principal user = accessor.getUser();

            if (user == null) {
                user = (Principal) accessor.getSessionAttributes().get("user");
                if (user != null) {
                    accessor.setUser(user); // setearlo de nuevo
                }
            }

            if (user == null) {
                throw new AccessDeniedException("No se pudo obtener el usuario en la suscripción");
            }

            String dest = accessor.getDestination(); // e.g. /topic/updates/Sala1
            String roomId = dest.substring(dest.lastIndexOf('/') + 1);
            String username = accessor.getUser().getName();
            Sala sala = salaRepository.findByIdWithUsers(Integer.parseInt(roomId)).orElseThrow( () -> new AccessDeniedException("No existe la sala"));
            Set<String> permitidos = sala.getUsers().stream().map(User::getUsername).collect(Collectors.toSet());
            String owner = sala.getOwner().getUsername();
            System.out.println("Aquiiiiiiiiiiii");
            System.out.println(!permitidos.contains(username) && !username.equals(owner));
            if (!permitidos.contains(username) && !username.equals(owner)) {
                throw new AccessDeniedException(
                        "Acceso denegado a la sala: " + roomId);
            }
        }

        return message;
    }

    public class StompPrincipal implements Principal {
        private final String name;

        public StompPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

}
