package com.software.spring.controller;

import com.software.spring.service.AdministradorService;
import com.software.spring.service.AnuncioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anuncio")
public class EliminarAnuncioController {

    private final AnuncioService anuncioService;
    private final AdministradorService administradorService;

    public EliminarAnuncioController(AnuncioService anuncioService, AdministradorService administradorService) {
        this.anuncioService = anuncioService;
        this.administradorService = administradorService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAnuncio(
            @PathVariable Integer id,
            @RequestHeader("X-Admin-Username") String adminUser,
            @RequestHeader("X-Admin-Password") String adminPass
    ) {
        try {
            administradorService.autenticarAdministrador(adminUser, adminPass);
            anuncioService.eliminarAnuncio(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo eliminar el anuncio: " + e.getMessage());
        }
    }
}
