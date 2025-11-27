package com.software.spring.controller;
import com.software.spring.model.Chat;
import com.software.spring.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat/crear")
public class CrearChatController {
    private final ChatService chatService;
    
    public CrearChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @PostMapping
    public ResponseEntity<Chat> crearChat(@RequestBody Map<String, List<String>> request) {
        List<String> usuarioIds = request.get("usuarioIds");
        Chat nuevoChat = chatService.crearChat(usuarioIds);
        return ResponseEntity.status(201).body(nuevoChat);
    }
}
