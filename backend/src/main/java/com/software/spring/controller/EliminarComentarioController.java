package com.software.spring.controller;

import com.software.spring.service.ComentarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentariospublicacion")
public class EliminarComentarioController {

    private final ComentarioService comentarioService;
    
    public EliminarComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Integer id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }
}
