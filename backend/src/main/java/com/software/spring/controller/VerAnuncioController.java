package com.software.spring.controller;

import com.software.spring.model.Anuncio;
import com.software.spring.service.AnuncioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anuncio")
public class VerAnuncioController {
    
    private final AnuncioService anuncioService;
    
    public VerAnuncioController(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }
    
    /**
     * Endpoint para obtener el anuncio activo actual
     * GET /api/anuncio
     */
    @GetMapping
    public ResponseEntity<?> obtenerAnuncioActivo() {
        Anuncio anuncio = anuncioService.obtenerAnuncioActivo();
        
        if (anuncio != null) {
            return ResponseEntity.ok(anuncio);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    
    /**
     * Endpoint para obtener un anuncio por ID
     * GET /api/anuncio/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAnuncioPorId(@PathVariable Integer id) {
        return anuncioService.obtenerAnuncioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
