package com.software.spring.repository;

import com.software.spring.model.entity.Anuncio;

import java.util.List;
import java.util.Optional;

public interface AnuncioRepository {
    List<Anuncio> findAll();
    Optional<Anuncio> findById(Integer id);
    Anuncio save(Anuncio anuncio);
    Optional<Anuncio> findLatestActive(); // Encuentra el anuncio activo más reciente
}
