package com.software.spring.controller.chatControladores;

import com.software.spring.model.entity.Mensaje;
import com.software.spring.service.MensajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mensaje/enviar")
public class EnviarMensaje {
    private final MensajeService mensajeService;
    
    public EnviarMensaje(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }
    
    @PostMapping
    public ResponseEntity<?> enviarMensaje(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== RECIBIENDO PETICIÓN ===");
            System.out.println("Request completo: " + request);
            
            Mensaje mensaje = new Mensaje();
            mensaje.setContenido((String) request.get("contenido"));
            mensaje.setAutorId((String) request.get("autorId"));
            mensaje.setChatId((Integer) request.get("chatId"));
            
            System.out.println("Contenido: " + mensaje.getContenido());
            System.out.println("AutorId: " + mensaje.getAutorId());
            System.out.println("ChatId: " + mensaje.getChatId());
            
            Mensaje enviado = mensajeService.enviarMensaje(mensaje);
            System.out.println("Mensaje guardado con ID: " + enviado.getId());
            return ResponseEntity.status(201).body(enviado);
        } catch (Exception e) {
            System.err.println("ERROR al enviar mensaje: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
