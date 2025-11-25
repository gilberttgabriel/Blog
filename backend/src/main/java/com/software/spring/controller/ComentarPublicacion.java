package com.software.spring.controller;

import com.software.spring.model.Comentario;
import com.software.spring.service.ComentarioService;
import com.software.spring.service.PublicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentariospublicacion")
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
    
    
    @GetMapping("/{publicacionId}")
    public ResponseEntity<List<Comentario>> verComentariosPorPublicacion(@PathVariable Integer publicacionId) {
        List<Comentario> comentarios = comentarioService.verComentariosPorPublicacion(publicacionId);
        return ResponseEntity.ok(comentarios);
    }
    
}
