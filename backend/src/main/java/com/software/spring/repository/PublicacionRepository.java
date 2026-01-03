package com.software.spring.repository;

import java.util.List;
import java.util.Optional;

import com.software.spring.model.Publicacion;

public interface PublicacionRepository {
    void save(Publicacion publicacion);
    void update(Publicacion publicacion);
    void delete(Integer id);

    Optional<Publicacion> findById(Integer id);
    Optional<Publicacion> findByTitulo(String titulo);
    List<Publicacion> findAll();
}

