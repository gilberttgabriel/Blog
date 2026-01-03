package com.software.spring.repository;

import java.util.List;

import com.software.spring.model.Comentario;

public interface ComentarioRepository {
    Comentario save(Comentario comentario);
    List<Comentario> findAll();
    List<Comentario> findByPublicacionId(Integer publicacionId);
    void deleteById(Integer id);
}
