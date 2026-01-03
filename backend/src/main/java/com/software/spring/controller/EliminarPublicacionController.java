package com.software.spring.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.software.spring.service.PublicacionService;

@RestController
@RequestMapping("/api/publicacion")
public class EliminarPublicacionController {

    private final PublicacionService publicacionService;

    public EliminarPublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPublicacion(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body
    ) {
        String autorId = body.get("autorId");

        publicacionService.eliminarPublicacion(id, autorId);
        return ResponseEntity.noContent().build();
    }
}
