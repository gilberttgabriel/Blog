package com.software.spring.controller;

import com.software.spring.model.Publicacion;
import com.software.spring.service.PublicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publicacion/crear")
public class CrearPublicacionController {
    private final PublicacionService PublicacionService;
    public CrearPublicacionController(PublicacionService PublicacionService) {
        this.PublicacionService = PublicacionService ;
    }
    @PostMapping
    public ResponseEntity<Publicacion> crearPublicacion(@RequestBody Publicacion publicacion) {
        Publicacion creada = PublicacionService.crearPublicacion(publicacion);
        return ResponseEntity.status(201).body(creada);
    }


}
