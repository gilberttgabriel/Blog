package com.software.spring.controller.usuarioControladores;

import com.software.spring.model.entity.Usuario;
import com.software.spring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/usuarios")
public class VerPerfilController {
    private final UsuarioService usuarioService;
    public VerPerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @GetMapping
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    public ResponseEntity<Usuario> verDetalle(@RequestBody String id) {
        Usuario detalleUsuario = usuarioService.verDetalleUsuario(id);
        return ResponseEntity.status(201).body(detalleUsuario);
    }


}
