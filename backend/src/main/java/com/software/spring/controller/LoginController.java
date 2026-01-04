package com.software.spring.controller;

import com.software.spring.model.Usuario;
import com.software.spring.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*")
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.get("username");
        String password = credenciales.get("contraseña");

        // Si el frontend envía "password" en lugar de "contraseña", lanza el mensaje
        // if (password == null) password = credenciales.get("password");

        try {
            Usuario usuario = usuarioService.autenticarUsuario(username, password);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            // Esto devuelve el mensaje "Este usuario no existe" o "Contraseña incorrecta"
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del servidor");
        }
    }
}