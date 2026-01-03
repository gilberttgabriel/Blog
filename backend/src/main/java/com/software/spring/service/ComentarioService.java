package com.software.spring.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.software.spring.model.Comentario;
import com.software.spring.repository.ComentarioRepository;

@Service
public class ComentarioService {

    private final ComentarioRepository repo;

    public ComentarioService(ComentarioRepository repo) {
        this.repo = repo;
    }

    public Comentario crearComentario(Comentario comentario) {

        if (!StringUtils.hasText(comentario.getContenido()) 
             )
             {
            throw new IllegalArgumentException("el campo comentario no puede estar vacio");
        }

        // Generar un ID único para el comentario
        Integer id = Math.abs(UUID.randomUUID().hashCode());
        
        Comentario nuevo = new Comentario(
            id,
            comentario.getContenido(),
            comentario.getAutor(),
            comentario.getPublicacionId(),
            comentario.getFechaCreacion()
        );
         return repo.save(nuevo);
    }

    public List<Comentario> verComentariosPorPublicacion(Integer publicacionId) {
        return repo.findByPublicacionId(publicacionId);
    }

    public void eliminarComentario(Integer id) {
        repo.deleteById(id);
    }

    public Comentario editarComentario(Integer id, String nuevoContenido, String autor) {

        if (!StringUtils.hasText(nuevoContenido)) {
            throw new IllegalArgumentException("El contenido del comentario no puede estar vacío");
        }

        Comentario comentario = repo.findAll().stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));

        // Validar que solo el autor pueda editar
        if (!comentario.getAutor().equals(autor)) {
            throw new IllegalArgumentException("No tienes permiso para editar este comentario");
        }

        comentario.setContenido(nuevoContenido); 
        // comentario.setFechaEdicion(LocalDateTime.now()); // opcional solo tienes que modificar el model

        return repo.save(comentario);
    }


}
