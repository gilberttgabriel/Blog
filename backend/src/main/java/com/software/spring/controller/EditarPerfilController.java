package com.software.spring.controller;

import com.software.spring.model.Usuario;
import com.software.spring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(originPatterns = "*")
public class EditarPerfilController {

    private final UsuarioService usuarioService;

    public EditarPerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // HU editar perfil
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarPerfil(@PathVariable String id, @RequestBody Usuario datos) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(id, datos);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al editar: " + e.getMessage());
        }
    }

    // HU eliminar
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPerfil(@PathVariable String id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar: " + e.getMessage());
        }
    }
}