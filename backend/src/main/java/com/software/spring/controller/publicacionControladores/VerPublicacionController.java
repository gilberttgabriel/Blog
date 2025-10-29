package com.software.spring.controller.publicacionControladores;

import com.software.spring.model.entity.Publicacion;
import com.software.spring.service.PublicacionService;
import com.software.spring.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicacion")
public class VerPublicacionController {
    private final PublicacionService publicacionService;
    private final UsuarioService usuarioService;
    
    public VerPublicacionController(PublicacionService publicacionService, UsuarioService usuarioService) {
        this.publicacionService = publicacionService;
        this.usuarioService = usuarioService;
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Publicacion> verPublicacion(@PathVariable Integer id) {
        Publicacion publicacion = publicacionService.verPublicacion(id);
        return ResponseEntity.ok(publicacion);
    }
    
    @GetMapping
    public ResponseEntity<List<Publicacion>> verTodasLasPublicaciones() {
        List<Publicacion> publicaciones = publicacionService.verTodasLasPublicaciones();
        return ResponseEntity.ok(publicaciones);
    }
    
    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<Publicacion>> verPublicacionesPorAutor(@PathVariable String autorId) {
        // Verificar que el autor existe
        usuarioService.obtenerPorId(autorId)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró el perfil con ID: " + autorId));
        
        // Buscar publicaciones del autor
        List<Publicacion> publicaciones = publicacionService.verPublicacionesPorAutorId(autorId);
        return ResponseEntity.ok(publicaciones);
    }
}
