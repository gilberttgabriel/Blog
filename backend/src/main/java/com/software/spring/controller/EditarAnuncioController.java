package com.software.spring.controller;

import com.software.spring.model.Anuncio;
import com.software.spring.service.AdministradorService;
import com.software.spring.service.AnuncioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anuncio")
public class EditarAnuncioController {

    private final AnuncioService anuncioService;
    private final AdministradorService administradorService;

    public EditarAnuncioController(AnuncioService anuncioService, AdministradorService administradorService) {
        this.anuncioService = anuncioService;
        this.administradorService = administradorService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarAnuncio(
            @PathVariable Integer id,
            @RequestBody Anuncio cambios,
            @RequestHeader("X-Admin-Username") String adminUser,
            @RequestHeader("X-Admin-Password") String adminPass
    ) {
        try {
            administradorService.autenticarAdministrador(adminUser, adminPass);
            Anuncio actualizado = anuncioService.editarAnuncio(id, cambios);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo editar el anuncio: " + e.getMessage());
        }
    }
}
