package com.software.spring.repository;

import java.util.Optional;

import com.software.spring.model.Administrador;

public interface AdministradorRepository {
    Optional<Administrador> findByUsername(String username);
}
