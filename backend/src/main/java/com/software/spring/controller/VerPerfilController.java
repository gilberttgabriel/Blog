package com.software.spring.controller;

import com.software.spring.model.entity.Usuario;
import com.software.spring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class VerPerfilController {
    private final UsuarioService usuarioService;
    public VerPerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @DeleteMapping
    public ResponseEntity<Usuario> verDetalle(@RequestBody String id) {
        Usuario detalleUsuario = usuarioService.verDetalleUsuario(id);
        return ResponseEntity.status(201).body(detalleUsuario);
    }


}
