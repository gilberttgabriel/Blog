package com.software.spring.controller;

import com.software.spring.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class EliminarChatController {

    private final ChatService chatService;

    public EliminarChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarChat(@PathVariable Integer id) {
        boolean eliminado = chatService.eliminarChat(id);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}