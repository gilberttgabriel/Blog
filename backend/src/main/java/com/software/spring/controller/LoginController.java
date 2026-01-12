package com.software.spring.controller;

import com.software.spring.model.Usuario;
import com.software.spring.model.Administrador;
import com.software.spring.service.UsuarioService;
import com.software.spring.service.AdministradorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = "*")
public class LoginController {

    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;

    public LoginController(UsuarioService usuarioService, AdministradorService administradorService) {
        this.usuarioService = usuarioService;
        this.administradorService = administradorService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String username = credenciales.get("username");
        String password = credenciales.get("contraseña");

        try {
            // Primero intentar autenticar como usuario normal
            Usuario usuario = usuarioService.autenticarUsuario(username, password);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            // Si falla, intentar autenticar como administrador
            try {
                Administrador admin = administradorService.autenticarAdministrador(username, password);
                // Devolver el objeto Administrador directamente para que el frontend lo reconozca
                return ResponseEntity.ok(admin);
            } catch (Exception adminException) {
                // Si ambos fallan, devolver el error original del usuario
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error del servidor");
        }
    }
}