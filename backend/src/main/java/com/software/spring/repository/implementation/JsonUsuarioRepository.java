

package com.software.spring.repository.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.software.spring.model.entity.Usuario;
import com.software.spring.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Repositorio que persiste usuarios en ./data/usuarios.json
 * Estructura del archivo: [ { ...Usuario... }, ... ]
 */
@Repository
public class JsonUsuarioRepository implements UsuarioRepository {

    private final ObjectMapper mapper;
    private final Path dbPath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonUsuarioRepository(
            ObjectMapper injectedMapper,
            @Value("${app.usuariosPath:./data/usuarios.json}") String configuredPath
    ) {
        this.mapper = injectedMapper.copy()
                .enable(SerializationFeature.INDENT_OUTPUT);

        this.dbPath = Paths.get(configuredPath);
        ensureDbExists();
    }

    /* ==========================
       Infra JSON
       ========================== */
    private void ensureDbExists() {
        try {
            if (dbPath.getParent() != null) {
                Files.createDirectories(dbPath.getParent());
            }
            if (Files.notExists(dbPath)) {
                writeUsuariosUnsafe(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar usuarios.json en " + dbPath, e);
        }
    }

    private List<Usuario> readUsuariosUnsafe() throws IOException {
        if (Files.size(dbPath) == 0L) {
            List<Usuario> empty = new ArrayList<>();
            writeUsuariosUnsafe(empty);
            return empty;
        }
        return mapper.readValue(dbPath.toFile(), new TypeReference<List<Usuario>>() {});
    }

    private void writeUsuariosUnsafe(List<Usuario> usuarios) throws IOException {
        mapper.writeValue(dbPath.toFile(), usuarios);
    }

    /* ==========================
       Implementación Repository
       ========================== */
    @Override
    public List<Usuario> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(readUsuariosUnsafe());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo usuarios desde " + dbPath, e);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<Usuario> findById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();
        return findAll().stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();
        return findAll().stream()
                .filter(u -> Objects.equals(u.getUsername(), username))
                .findFirst();
    }

    @Override
    public Usuario save(Usuario usuario) {
        lock.writeLock().lock();
        try {
            List<Usuario> usuarios = readUsuariosUnsafe();
            // Reemplaza si existe por id, si no, agrega
            usuarios = usuarios.stream()
                    .filter(u -> !Objects.equals(u.getId(), usuario.getId()))
                    .collect(Collectors.toCollection(ArrayList::new));
            usuarios.add(usuario);
            writeUsuariosUnsafe(usuarios);
            return usuario;
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo usuarios en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.isBlank()) return;
        lock.writeLock().lock();
        try {
            List<Usuario> usuarios = readUsuariosUnsafe();
            boolean changed = usuarios.removeIf(u -> Objects.equals(u.getId(), id));
            if (changed) {
                writeUsuariosUnsafe(usuarios);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error eliminando usuario en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
