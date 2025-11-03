package com.software.spring.controller.usuarioControladores;

import com.software.spring.controller.usuarioControladores.LoginController.ErrorResponse;
import com.software.spring.controller.usuarioControladores.LoginController.LoginResponse;
import com.software.spring.model.entity.Usuario;
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
            
            // Crear respuesta sin exponer la contraseña
            LoginResponse response = new LoginResponse(
                nuevoUsuario.getId(),
                nuevoUsuario.getUsername(),
                nuevoUsuario.getNombre(),
                nuevoUsuario.getApellido(),
                nuevoUsuario.getDescripcion(),
                nuevoUsuario.getEdad(),
                "Usuario registrado exitosamente"
            );
            
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(400)
                .body(new ErrorResponse("Error en el registro: " + e.getMessage()));
        }
    }

}
