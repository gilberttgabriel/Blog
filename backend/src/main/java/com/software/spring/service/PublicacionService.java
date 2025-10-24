package com.software.spring.service;

import com.software.spring.model.entity.Publicacion;
import com.software.spring.repository.PublicacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.UUID;

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
        String id = UUID.randomUUID().toString();
        
        Publicacion nuevo = new Publicacion(
             Integer.parseInt(id),
             publicacion.getTitulo(),
             publicacion.getContenido(),
             publicacion.getAutor(),
             publicacion.getFechaCreacion()
    );
        // Guardar la publicación
        return repo.save(nuevo);
    }

    public Publicacion verPublicacion(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontró la publicación con ID: " + id));
    }

    public java.util.List<Publicacion> verTodasLasPublicaciones() {
        return repo.findAll();
    }

    public java.util.List<Publicacion> verPublicacionesPorAutor(com.software.spring.model.entity.Perfil autor) {
        return repo.findAll().stream()
            .filter(p -> p.getAutor() != null && p.getAutor().equals(autor))
            .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<Publicacion> verPublicacionesPorAutorId(String autorId) {
        return repo.findAll().stream()
            .filter(p -> p.getAutor() != null && autorId.equals(p.getAutor().getId()))
            .collect(java.util.stream.Collectors.toList());
    }

}
