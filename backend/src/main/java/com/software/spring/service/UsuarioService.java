package com.software.spring.service;

import com.software.spring.model.entity.Usuario;
import com.software.spring.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }
    public Usuario crearUsuario(Usuario usuario) {

        // Validaciones mínimas
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

        // Unicidad de username
        repo.findByUsername(usuario.getUsername()).ifPresent(u -> {
            throw new IllegalStateException("Ya existe un usuario con ese username");
        });

        // Generar valores del perfil
        String id = UUID.randomUUID().toString();
        boolean activo = true;
        LocalDateTime ultimoAcceso = LocalDateTime.now();

        // Si quieres hashear: ej. BCrypt (spring-security-crypto)
        // String hash = BCrypt.hashpw(contraseña, BCrypt.gensalt());

        Usuario nuevo = new Usuario(
                id,
                usuario.getUsername(),
                usuario.getContraseña(), // o hash
                usuario.getNombre(),
                usuario.getApellido(),
                activo,
                ultimoAcceso,
                usuario.getDescripcion(),
                usuario.getEdad()
        );

        return repo.save(nuevo);
    }

    /* ==========================
       Lecturas
       ========================== */
    public List<Usuario> listarUsuarios() {
        return repo.findAll();
    }
    
    public List<Usuario> obtenerTodosLosUsuarios() {
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

    /* ==========================
       Autenticación (Login)
       ========================== */
    public Usuario autenticarUsuario(String username, String contraseña) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(contraseña)) {
            throw new IllegalArgumentException("Username y contraseña son obligatorios");
        }

        Usuario usuario = repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        // Verificar contraseña (en producción deberías usar BCrypt)
        if (!usuario.getContraseña().equals(contraseña)) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // Actualizar último acceso
        usuario.setUltimoAcceso(LocalDateTime.now());

        return usuario;
    }
    public void registrarAcceso(String id) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));

        // Si Perfil tiene setter:
        // existente.setUltimoAcceso(LocalDateTime.now());

        repo.save(existente);
    }
    public Usuario verDetalleUsuario(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
    }
}
