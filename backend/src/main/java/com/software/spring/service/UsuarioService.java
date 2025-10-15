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
    public Usuario crearUsuario(String username,
                                String contraseña,
                                String nombre,
                                String apellido,
                                String descripcion,
                                Integer edad) {

        // Validaciones mínimas
        if (!StringUtils.hasText(username) ||
            !StringUtils.hasText(contraseña) ||
            !StringUtils.hasText(nombre) ||
            !StringUtils.hasText(apellido)) {
            throw new IllegalArgumentException("username, contraseña, nombre y apellido son obligatorios");
        }

        if (edad != null && (edad < 0 || edad > 120)) {
            throw new IllegalArgumentException("edad inválida");
        }

        // Unicidad de username
        repo.findByUsername(username).ifPresent(u -> {
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
                username,
                contraseña, // o hash
                nombre,
                apellido,
                activo,
                ultimoAcceso,
                descripcion,
                edad
        );

        return repo.save(nuevo);
    }

    /* ==========================
       Lecturas
       ========================== */
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

    /* ==========================
       Actualizaciones
       ========================== */
    public Usuario actualizarDescripcionYEdad(String id, String nuevaDescripcion, Integer nuevaEdad) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));

        if (nuevaEdad != null && (nuevaEdad < 0 || nuevaEdad > 120)) {
            throw new IllegalArgumentException("edad inválida");
        }

        // Solo toca campos propios de Usuario (no Perfil)
        if (nuevaDescripcion != null) {
            existente.setDescripcion(nuevaDescripcion);
        }
        if (nuevaEdad != null) {
            existente.setEdad(nuevaEdad);
        }

        // Si quieres registrar actividad:
        // existente.setUltimoAcceso(LocalDateTime.now());  // si Perfil expone setter

        return repo.save(existente);
    }

    /**
     * Ejemplo de “actualización de acceso” (p. ej. al loguearse).
     * Llama esto cuando el usuario inicia sesión para actualizar último acceso.
     * Requiere que Perfil tenga setter para ultimoAcceso.
     */
    public void registrarAcceso(String id) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));

        // Si Perfil tiene setter:
        // existente.setUltimoAcceso(LocalDateTime.now());

        repo.save(existente);
    }

    /* ==========================
       Borrado
       ========================== */
    public void eliminarPorId(String id) {
        // Puedes validar reglas (ej. no permitir borrar admins, etc.)
        repo.deleteById(id);
    }
}
