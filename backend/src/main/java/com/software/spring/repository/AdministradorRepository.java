package com.software.spring.repository;

import com.software.spring.model.entity.Administrador;

import java.util.Optional;

public interface AdministradorRepository {
    Optional<Administrador> findByUsername(String username);
}
