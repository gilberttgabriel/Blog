package com.software.spring.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.software.spring.model.Publicacion;
import com.software.spring.repository.PublicacionRepository;

@Service
public class PublicacionService {

    private final PublicacionRepository repo;
    
    public PublicacionService(PublicacionRepository repo) {
        this.repo = repo;
    }

    public Publicacion crearPublicacion(Publicacion publicacion) {
        // Validaciones mínimas
        if (!StringUtils.hasText(publicacion.getContenido()) ||
            !StringUtils.hasText(publicacion.getTitulo())
             )
             {
            throw new IllegalArgumentException("titulo y contenido son campos obligatorios");
        }

        // Verificar si ya existe una publicación con el mismo título
        repo.findByTitulo(publicacion.getTitulo()).ifPresent(u -> {
            throw new IllegalStateException("Ya existe una publicacion con ese titulo");
        });

        // Establecer fecha de creación si no está establecida
        if (publicacion.getFechaCreacion() == null) {
            publicacion.setFechaCreacion(LocalDateTime.now());
        }
        
        // Generar un ID único para la publicación
        Integer id = Math.abs(UUID.randomUUID().hashCode());
        
        Publicacion nuevo = new Publicacion(
             id,
             publicacion.getTitulo(),
             publicacion.getContenido(),
             publicacion.getAutorId(),
             publicacion.getFechaCreacion()
        );
        // Guardar la publicación
        repo.save(nuevo);
        return nuevo;
    }

    public Publicacion verPublicacion(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la publicación con ID: " + id));
    }

    public java.util.List<Publicacion> verTodasLasPublicaciones() {
        return repo.findAll();
    }

    public java.util.List<Publicacion> verPublicacionesPorAutorId(String autorId) {
        return repo.findAll().stream()
            .filter(p -> p.getAutorId() != null && autorId.equals(p.getAutorId()))
            .collect(java.util.stream.Collectors.toList());
    }
    
    public Publicacion editarPublicacion(
        Integer id,
        String titulo,
        String contenido,
        String autorId
    ) {
        Publicacion existente = repo.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("No existe la publicación con ID: " + id)
                );

        if (!existente.getAutorId().equals(autorId)) {
            throw new IllegalStateException("No tienes permiso para editar esta publicación");
        }

        existente.setTitulo(titulo);
        existente.setContenido(contenido);

        repo.update(existente);

        return existente;
    }


    public void eliminarPublicacion(Integer id, String autorId) {
        Publicacion existente = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publicación no encontrada"));

        if (!existente.getAutorId().equals(autorId)) {
            throw new IllegalStateException("No tienes permiso para eliminar esta publicación");
        }

        repo.delete(id);
    }

}
