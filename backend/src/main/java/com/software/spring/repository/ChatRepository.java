package com.software.spring.repository;

import java.util.List;
import java.util.Optional;

import com.software.spring.model.Chat;

public interface ChatRepository {
    void save(Chat chat);
    void update(Chat chat);
    Optional<Chat> findById(Integer id);
    List<Chat> findAll();
    List<Chat> findByUsuarioId(String usuarioId);
}
