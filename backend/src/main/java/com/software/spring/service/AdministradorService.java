package com.software.spring.service;

import com.software.spring.model.Administrador;
import com.software.spring.repository.AdministradorRepository;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {
    
    private final AdministradorRepository administradorRepository;
    
    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }
    
    /**
     * Autentica un administrador
     */
    public Administrador autenticarAdministrador(String username, String contraseña) {
        Administrador admin = administradorRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos"));
        
        if (!admin.getContraseña().equals(contraseña)) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }
        
        if (!admin.isActivo()) {
            throw new IllegalStateException("Administrador inactivo");
        }
        
        return admin;
    }
}
