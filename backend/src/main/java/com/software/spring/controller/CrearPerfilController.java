package com.software.spring.controller;

import com.software.spring.model.Usuario;
import com.software.spring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/crear")
public class CrearPerfilController {
    private final UsuarioService usuarioService;
    public CrearPerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            
            return ResponseEntity.status(201).body(nuevoUsuario);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(400)
                .body("Error en el registro: " + e.getMessage());
        }
    }

}
