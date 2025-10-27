package com.software.spring.controller.comentarioControladores;

import com.software.spring.model.entity.Comentario;
import com.software.spring.service.ComentarioService;
import com.software.spring.service.PublicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarPublicacion {

    private final ComentarioService comentarioService;
    private final PublicacionService publicacionService;
    
    public ComentarPublicacion(ComentarioService comentarioService, PublicacionService publicacionService) {
        this.comentarioService = comentarioService;
        this.publicacionService = publicacionService;
    }
    
    @PostMapping
    public ResponseEntity<Comentario> crearComentario(@RequestBody Comentario comentario) {
        // Verificar que la publicación existe
        publicacionService.verPublicacion(comentario.getPublicacionId());
        
        Comentario creado = comentarioService.crearComentario(comentario);
        return ResponseEntity.status(201).body(creado);
    }
    
    
    @GetMapping("/publicacion/{publicacionId}")
    public ResponseEntity<List<Comentario>> verComentariosPorPublicacion(@PathVariable Integer publicacionId) {
        // Verificar que la publicación existe
        publicacionService.verPublicacion(publicacionId);
        
        List<Comentario> comentarios = comentarioService.verComentariosPorPublicacion(publicacionId);
        return ResponseEntity.ok(comentarios);
    }
    
}
