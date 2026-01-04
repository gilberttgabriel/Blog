package com.software.spring.service;

import com.software.spring.model.Usuario;
import com.software.spring.repository.UsuarioRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    // [MODIFICACIÓN] Inyectamos el servicio de publicaciones para poder borrar en cascada
    private final PublicacionService publicacionService;

    public UsuarioService(UsuarioRepository repo, @Lazy PublicacionService publicacionService) {
        this.repo = repo;
        this.publicacionService = publicacionService;
    }

    // ---------------------------------------------------------------
    // CÓDIGO ORIGINAL
    // ---------------------------------------------------------------

    public Usuario crearUsuario(Usuario usuario) {
        if (!StringUtils.hasText(usuario.getUsername()) ||
                !StringUtils.hasText(usuario.getApellido()) ||
                !StringUtils.hasText(usuario.getNombre()) ||
                !StringUtils.hasText(usuario.getContraseña()) ||
                !StringUtils.hasText(usuario.getDescripcion())||
                !StringUtils.hasText(String.valueOf(usuario.getEdad())))
        {
            throw new IllegalArgumentException("username, contraseña, nombre y apellido son obligatorios");
        }

        if (usuario.getEdad() < 18 || usuario.getEdad()  > 120) {
            throw new IllegalArgumentException("edad inválida");
        }

        repo.findByUsername(usuario.getUsername()).ifPresent(u -> {
            throw new IllegalStateException("Ya existe un usuario con ese username");
        });

        String id = UUID.randomUUID().toString();
        boolean activo = true;
        LocalDateTime ultimoAcceso = LocalDateTime.now();

        Usuario nuevo = new Usuario(
                id,
                usuario.getUsername(),
                usuario.getContraseña(),
                usuario.getNombre(),
                usuario.getApellido(),
                activo,
                ultimoAcceso,
                usuario.getDescripcion(),
                usuario.getEdad()
        );

        return repo.save(nuevo);
    }

    public List<Usuario> listarUsuarios() {
        return repo.findAll();
    }

    public Optional<Usuario> obtenerPorId(String id) {
        if (!StringUtils.hasText(id)) return Optional.empty();
        return repo.findById(id);
    }

    public Optional<Usuario> obtenerPorUsername(String username) {
        if (!StringUtils.hasText(username)) return Optional.empty();
        return repo.findByUsername(username);
    }

    public Usuario autenticarUsuario(String username, String contraseña) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(contraseña)) {
            throw new IllegalArgumentException("Username y contraseña son obligatorios");
        }

        Usuario usuario = repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Este usuario no existe"));

        if (!usuario.getContraseña().equals(contraseña)) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        usuario.setUltimoAcceso(LocalDateTime.now());
        return usuario;
    }

    public Usuario verDetalleUsuario(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
    }

    // ---------------------------------------------------------------
    // HU Editar y Eliminar
    // ---------------------------------------------------------------

    public Usuario actualizarUsuario(String id, Usuario datos) {
        Usuario existente = verDetalleUsuario(id);
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setUsername(datos.getUsername());
        existente.setDescripcion(datos.getDescripcion());
        return repo.save(existente);
    }

    public void eliminarUsuario(String id) {
        // [MODIFICACIÓN] Primero eliminamos las publicaciones asociadas (Cascada)
        publicacionService.eliminarPublicacionesPorAutorId(id);

        // Luego eliminamos al usuario
        repo.deleteById(id);
    }
}