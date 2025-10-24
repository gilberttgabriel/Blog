package com.software.spring.controller.publicacionControladores;

import com.software.spring.model.entity.Publicacion;
import com.software.spring.service.PublicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicacion")
public class VerPublicacionController {
    private final PublicacionService publicacionService;
    
    public VerPublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
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
        // Necesitarías crear un método para buscar Perfil por ID
        // Por ahora, este endpoint está preparado para futuras implementaciones
        return ResponseEntity.notFound().build();
    }
}
