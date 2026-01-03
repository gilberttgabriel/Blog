package com.software.spring.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.software.spring.model.Publicacion;
import com.software.spring.service.PublicacionService;

@RestController
@RequestMapping("/api/publicacion")
public class EditarPublicacionController {

    private final PublicacionService publicacionService;

    public EditarPublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publicacion> editarPublicacion(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body
    ) {
        String titulo = body.get("titulo");
        String contenido = body.get("contenido");
        String autorId = body.get("autorId");

        Publicacion editada =
                publicacionService.editarPublicacion(id, titulo, contenido, autorId);

        return ResponseEntity.ok(editada);
    }
}
