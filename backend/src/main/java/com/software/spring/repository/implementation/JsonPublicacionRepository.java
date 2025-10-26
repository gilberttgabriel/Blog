package com.software.spring.repository.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.software.spring.model.BaseDeDatos;
import com.software.spring.model.entity.Publicacion;
import com.software.spring.repository.PublicacionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Repositorio que persiste publicaciones en ./data/db.json bajo la clave "publicaciones".
 * Estructura del archivo:
 * {
 *   "publicaciones": [ { ...Publicacion... }, ... ]
 * }
 *
 * Requisitos: Publicacion debe ser deserializable por Jackson
 * (constructor vacío o @JsonCreator + @JsonProperty).
 */
@Repository
public class JsonPublicacionRepository implements PublicacionRepository {

    private final ObjectMapper mapper;
    private final Path dbPath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonPublicacionRepository(
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
                BaseDeDatos empty = new BaseDeDatos();
                writeDbUnsafe(empty);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar db.json en " + dbPath, e);
        }
    }

    private BaseDeDatos readDbUnsafe() throws IOException {
        if (Files.size(dbPath) == 0L) {
            BaseDeDatos empty = new BaseDeDatos();
            writeDbUnsafe(empty);
            return empty;
        }
        return mapper.readValue(dbPath.toFile(), BaseDeDatos.class);
    }

    private void writeDbUnsafe(BaseDeDatos db) throws IOException {
        mapper.writeValue(dbPath.toFile(), db);
    }

    /* ==========================
       Implementación Repository
       ========================== */
    @Override
    public List<Publicacion> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(readDbUnsafe().publicaciones);
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo publicaciones desde " + dbPath, e);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<Publicacion> findById(Integer id) {
        if (id == null) return Optional.empty();
        return findAll().stream()
                .filter(p -> Objects.equals(p.getId(), id))
                .findFirst();
    }

    @Override
    public Optional<Publicacion> findByTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) return Optional.empty();
        return findAll().stream()
                .filter(p -> Objects.equals(p.getTitulo(), titulo))
                .findFirst();
    }

    @Override
    public Publicacion save(Publicacion publicacion) {
        lock.writeLock().lock();
        try {
            BaseDeDatos db = readDbUnsafe();
            // Reemplaza si existe por id, si no, agrega
            db.publicaciones = db.publicaciones.stream()
                    .filter(p -> !Objects.equals(p.getId(), publicacion.getId()))
                    .collect(Collectors.toCollection(ArrayList::new));
            db.publicaciones.add(publicacion);
            writeDbUnsafe(db);
            return publicacion;
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo publicaciones en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
