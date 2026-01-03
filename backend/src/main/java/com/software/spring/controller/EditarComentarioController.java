package com.software.spring.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.software.spring.model.Comentario;
import com.software.spring.service.ComentarioService;

@RestController
@RequestMapping("/api/comentariospublicacion")
@CrossOrigin(origins = "http://localhost:5173")
public class EditarComentarioController {

    private final ComentarioService comentarioService;

    public EditarComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comentario> editarComentario(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body
    ) {
        String contenido = body.get("contenido");
        String autor = body.get("autor");

        Comentario comentarioEditado =
                comentarioService.editarComentario(id, contenido, autor);

        return ResponseEntity.ok(comentarioEditado);
    }
}
