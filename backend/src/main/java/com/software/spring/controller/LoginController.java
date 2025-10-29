package com.software.spring.controller;

import com.software.spring.model.entity.Usuario;
import com.software.spring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    
    private final UsuarioService usuarioService;
    
    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Endpoint para login
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Usuario usuario = usuarioService.autenticarUsuario(
                request.getUsername(), 
                request.getContraseña()
            );
            
            // Crear respuesta sin exponer la contraseña
            LoginResponse response = new LoginResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getApellido(),
                "Login exitoso"
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401)
                .body(new ErrorResponse("Error de autenticación: " + e.getMessage()));
        }
    }
    
    /**
     * Endpoint para registro de nuevo usuario
     * POST /api/auth/register
     */

    
    // Clases internas para las peticiones y respuestas
    
    public static class LoginRequest {
        private String username;
        private String contraseña;
        
        public LoginRequest() {}
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getContraseña() { return contraseña; }
        public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    }
    
    public static class LoginResponse {
        private String id;
        private String username;
        private String nombre;
        private String apellido;
        private String descripcion;
        private Integer edad;
        private String mensaje;
        
        // Constructor para login (sin descripción y edad)
        public LoginResponse(String id, String username, String nombre, String apellido, String mensaje) {
            this.id = id;
            this.username = username;
            this.nombre = nombre;
            this.apellido = apellido;
            this.mensaje = mensaje;
        }
        
        // Constructor completo para registro (con descripción y edad)
        public LoginResponse(String id, String username, String nombre, String apellido, String descripcion, Integer edad, String mensaje) {
            this.id = id;
            this.username = username;
            this.nombre = nombre;
            this.apellido = apellido;
            this.descripcion = descripcion;
            this.edad = edad;
            this.mensaje = mensaje;
        }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        
        public Integer getEdad() { return edad; }
        public void setEdad(Integer edad) { this.edad = edad; }
        
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
    
    public static class ErrorResponse {
        private String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}



