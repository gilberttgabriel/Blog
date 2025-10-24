package com.software.spring.service;

import com.software.spring.model.entity.Comentario;
import com.software.spring.repository.ComentarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.UUID;

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

        String id = UUID.randomUUID().toString();
        
        Comentario nuevo = new Comentario(
            Integer.parseInt(id),
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
}
