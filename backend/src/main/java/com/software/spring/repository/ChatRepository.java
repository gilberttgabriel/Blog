package com.software.spring.repository;

import com.software.spring.model.entity.Chat;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    void save(Chat chat);
    Optional<Chat> findById(Integer id);
    List<Chat> findAll();
    List<Chat> findByUsuarioId(String usuarioId);
}
