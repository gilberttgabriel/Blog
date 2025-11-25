package com.software.spring.repository;

import java.util.List;
import java.util.Optional;

import com.software.spring.model.Anuncio;

public interface AnuncioRepository {
    List<Anuncio> findAll();
    Optional<Anuncio> findById(Integer id);
    Anuncio save(Anuncio anuncio);
    Optional<Anuncio> findLatestActive(); // Encuentra el anuncio activo más reciente
}
