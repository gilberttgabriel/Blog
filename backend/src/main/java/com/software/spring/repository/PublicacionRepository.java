package com.software.spring.repository;

import com.software.spring.model.entity.Publicacion;
import java.util.Optional;
import java.util.List;

public interface PublicacionRepository {
    Publicacion save(Publicacion publicacion);
    Optional<Publicacion> findById(Integer id);
    Optional<Publicacion> findByTitulo(String titulo);
    List<Publicacion> findAll();
}
