package com.software.spring.repository;

import com.software.spring.model.Comentario;
import java.util.List;

public interface ComentarioRepository {
    Comentario save(Comentario comentario);
    List<Comentario> findAll();
    List<Comentario> findByPublicacionId(Integer publicacionId);
    void deleteById(Integer id);
}
