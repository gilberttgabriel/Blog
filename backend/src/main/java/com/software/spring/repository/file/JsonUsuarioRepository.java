

package com.software.spring.repository.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
 * Repositorio que persiste usuarios en ./data/db.json bajo la clave "usuarios".
 * Estructura del archivo:
 * {
 *   "usuarios": [ { ...Usuario... }, ... ]
 * }
 *
 * Requisitos: Usuario (y su superclase Perfil) deben ser deserializables por Jackson
 * (constructor vacío o @JsonCreator + @JsonProperty).
 */
@Repository
public class JsonUsuarioRepository implements UsuarioRepository {

    private final ObjectMapper mapper;
    private final Path dbPath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // Wrapper interno para mapear el JSON
    private static class Db {
        public List<Usuario> usuarios = new ArrayList<>();
    }

    public JsonUsuarioRepository(
            ObjectMapper injectedMapper,
            @Value("${app.dbPath:./data/db.json}") String configuredPath
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
                // Si tienes un seed en resources, podrías copiarlo aquí.
                Db empty = new Db();
                writeDbUnsafe(empty);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar db.json en " + dbPath, e);
        }
    }

    private Db readDbUnsafe() throws IOException {
        if (Files.size(dbPath) == 0L) {
            Db empty = new Db();
            writeDbUnsafe(empty);
            return empty;
        }
        return mapper.readValue(dbPath.toFile(), Db.class);
    }

    private void writeDbUnsafe(Db db) throws IOException {
        mapper.writeValue(dbPath.toFile(), db);
    }

    /* ==========================
       Implementación Repository
       ========================== */
    @Override
    public List<Usuario> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(readDbUnsafe().usuarios);
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
            Db db = readDbUnsafe();
            // Reemplaza si existe por id, si no, agrega
            db.usuarios = db.usuarios.stream()
                    .filter(u -> !Objects.equals(u.getId(), usuario.getId()))
                    .collect(Collectors.toCollection(ArrayList::new));
            db.usuarios.add(usuario);
            writeDbUnsafe(db);
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
            Db db = readDbUnsafe();
            boolean changed = db.usuarios.removeIf(u -> Objects.equals(u.getId(), id));
            if (changed) {
                writeDbUnsafe(db);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error eliminando usuario en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
