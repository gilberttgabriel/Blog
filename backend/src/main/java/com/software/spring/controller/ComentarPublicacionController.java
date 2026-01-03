package com.software.spring.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.software.spring.model.Comentario;
import com.software.spring.service.ComentarioService;
import com.software.spring.service.PublicacionService;

@RestController
@RequestMapping("/api/comentariospublicacion")
@CrossOrigin(origins = "http://localhost:5173")
public class ComentarPublicacionController {

    private final ComentarioService comentarioService;
    private final PublicacionService publicacionService;
    
    public ComentarPublicacionController(ComentarioService comentarioService, PublicacionService publicacionService) {
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
