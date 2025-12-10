package com.software.spring.repository;

import com.software.spring.model.Mensaje;
import java.util.List;
import java.util.Optional;

public interface MensajeRepository {
    void save(Mensaje mensaje);
    Optional<Mensaje> findById(Integer id);
    List<Mensaje> findAll();
    List<Mensaje> findByChatId(Integer chatId);
}
