package com.software.spring.controller.usuarioControladores;

import com.software.spring.model.entity.Usuario;
import com.software.spring.model.entity.Administrador;
import com.software.spring.model.entity.LoginRequest;
import com.software.spring.service.UsuarioService;
import com.software.spring.service.AdministradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    
    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;
    
    public LoginController(UsuarioService usuarioService, AdministradorService administradorService) {
        this.usuarioService = usuarioService;
        this.administradorService = administradorService;
    }
    
    /**
     * Endpoint para login (usuarios y administradores)
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Primero intentar como usuario normal
            try {
                Usuario usuario = usuarioService.autenticarUsuario(
                    request.getUsername(), 
                    request.getContraseña()
                );
                
                return ResponseEntity.ok(usuario);
            } catch (Exception e) {
                // Si no es usuario, intentar como administrador
                Administrador admin = administradorService.autenticarAdministrador(
                    request.getUsername(), 
                    request.getContraseña()
                );
                
                return ResponseEntity.ok(admin);
            }
        } catch (Exception e) {
            return ResponseEntity.status(401)
                .body("Error de autenticación: " + e.getMessage());
        }
    }
    
    
}



