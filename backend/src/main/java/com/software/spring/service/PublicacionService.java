package com.software.spring.service;

import com.software.spring.model.Publicacion;
import com.software.spring.repository.PublicacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublicacionService {

    private final PublicacionRepository repo;

    public PublicacionService(PublicacionRepository repo) {
        this.repo = repo;
    }

    // --------------------------------------------------------
    // MÉTODOS DE CREACIÓN Y LECTURA
    // --------------------------------------------------------

    public Publicacion crearPublicacion(Publicacion publicacion) {
        if (!StringUtils.hasText(publicacion.getContenido()) ||
                !StringUtils.hasText(publicacion.getTitulo())) {
            throw new IllegalArgumentException("titulo y contenido son campos obligatorios");
        }

        repo.findByTitulo(publicacion.getTitulo()).ifPresent(u -> {
            throw new IllegalStateException("Ya existe una publicacion con ese titulo");
        });

        if (publicacion.getFechaCreacion() == null) {
            publicacion.setFechaCreacion(LocalDateTime.now());
        }

        Integer id = Math.abs(UUID.randomUUID().hashCode());

        Publicacion nuevo = new Publicacion(
                id,
                publicacion.getTitulo(),
                publicacion.getContenido(),
                publicacion.getAutorId(),
                publicacion.getFechaCreacion()
        );

        repo.save(nuevo);
        return nuevo;
    }

    public Publicacion verPublicacion(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la publicación con ID: " + id));
    }

    public List<Publicacion> verTodasLasPublicaciones() {
        return repo.findAll();
    }

    public List<Publicacion> verPublicacionesPorAutorId(String autorId) {
        return repo.findAll().stream()
                .filter(p -> p.getAutorId() != null && autorId.equals(p.getAutorId()))
                .collect(Collectors.toList());
    }




    public Publicacion editarPublicacion(Integer id, String titulo, String contenido, String autorId) {
        Publicacion existente = verPublicacion(id);


        if (existente.getAutorId() != null && !existente.getAutorId().equals(autorId)) {
            throw new IllegalArgumentException("No tienes permiso para editar esta publicación");
        }

        existente.setTitulo(titulo);
        existente.setContenido(contenido);

        // Usamos update del repositorio
        repo.update(existente);

        return existente;
    }


    public void eliminarPublicacion(Integer id, String autorId) {
        Publicacion existente = verPublicacion(id);

        // Verificamos que quien elimina sea el dueño
        if (existente.getAutorId() != null && !existente.getAutorId().equals(autorId)) {
            throw new IllegalArgumentException("No tienes permiso para eliminar esta publicación");
        }


        repo.delete(id);
    }

    // --------------------------------------------------------
    // Eliminacion de publicacion al eliminar el perfil
    // --------------------------------------------------------
    public void eliminarPublicacionesPorAutorId(String autorId) {
        List<Publicacion> publicacionesUsuario = verPublicacionesPorAutorId(autorId);

        for (Publicacion p : publicacionesUsuario) {
            repo.delete(p.getId());
        }
    }
}