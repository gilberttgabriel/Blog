package com.software.spring.repository;

import com.software.spring.model.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    List<Usuario> findAll();
    Optional<Usuario> findById(String id);
    Optional<Usuario> findByUsername(String username);
    Usuario save(Usuario usuario);
    void deleteById(String id);
}
