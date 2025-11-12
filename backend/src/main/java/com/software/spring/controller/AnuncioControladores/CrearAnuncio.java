package com.software.spring.controller.AnuncioControladores;

import com.software.spring.model.entity.Anuncio;
import com.software.spring.service.AnuncioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anuncio")
public class CrearAnuncio {
    
    private final AnuncioService anuncioService;
    
    public CrearAnuncio(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }
    
    @PostMapping("/crear")
    public ResponseEntity<?> crearAnuncio(@RequestBody Anuncio anuncio) {
        try {
            Anuncio nuevoAnuncio = anuncioService.crearAnuncio(anuncio);
            return ResponseEntity.status(201).body(nuevoAnuncio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .body("Error al crear anuncio: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error interno del servidor");
        }
    }
}
