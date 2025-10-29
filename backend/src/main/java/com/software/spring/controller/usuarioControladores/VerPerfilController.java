package com.software.spring.controller.usuarioControladores;

import com.software.spring.model.entity.Usuario;
import com.software.spring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class VerPerfilController {
    private final UsuarioService usuarioService;
    public VerPerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable String id) {
        Usuario usuario = usuarioService.verDetalleUsuario(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }
}