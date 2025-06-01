package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.demo.model.Message;

@Controller
public class CrudSyncController {
    
    @MessageMapping("/update/{roomId}")
    @SendTo("/topic/updates/{roomId}")
    public Message handleUpdate(
            @DestinationVariable Integer roomId,
            Message action) {

        System.out.printf("Usuario '%s' envía acción '%s' en sala %s%n",
                action.getSenderId(),
                action.getAction().getType(),
                roomId);
        return action;
    }
}
