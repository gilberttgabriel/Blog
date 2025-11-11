package com.software.spring.controller.chatControladores;

import com.software.spring.model.entity.Chat;
import com.software.spring.model.entity.Mensaje;
import com.software.spring.service.ChatService;
import com.software.spring.service.MensajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class VerChats {
    private final ChatService chatService;
    private final MensajeService mensajeService;
    
    public VerChats(ChatService chatService, MensajeService mensajeService) {
        this.chatService = chatService;
        this.mensajeService = mensajeService;
    }
    
    // GET /api/chat/{chatId} - Ver un chat específico con sus mensajes
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<Map<String, Object>> verChat(@PathVariable Integer chatId, @RequestParam(required = true) String usuarioId) {
        Chat chat = chatService.verChat(chatId);
        if (!chat.getUsuarioIds().contains(usuarioId)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "No tienes permiso para acceder a este chat");
            return ResponseEntity.status(403).body(errorResponse);
        }
        List<Mensaje> mensajes = mensajeService.verMensajesPorChatId(chatId);

        Map<String, Object> response = new HashMap<>();
        response.put("chat", chat);
        response.put("mensajes", mensajes);

        return ResponseEntity.ok(response);
    }
    
    // GET /api/chats/{usuarioId} - Ver todos los chats de un usuario
    @GetMapping("/chats/{usuarioId}")
    public ResponseEntity<List<Chat>> verChatsPorUsuario(@PathVariable String usuarioId) {
        List<Chat> chats = chatService.verChatsPorUsuarioId(usuarioId);
        return ResponseEntity.ok(chats);
    }
}

