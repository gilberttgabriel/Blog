package com.software.spring.controller;

import com.software.spring.model.Mensaje;
import com.software.spring.service.MensajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mensaje/enviar")
public class EnviarMensaje {
    private final MensajeService mensajeService;
    
    public EnviarMensaje(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }
    
    @PostMapping
    public ResponseEntity<?> enviarMensaje(@RequestBody Mensaje mensaje) {
        try {
            Mensaje enviado = mensajeService.enviarMensaje(mensaje);
            return ResponseEntity.status(201).body(enviado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
