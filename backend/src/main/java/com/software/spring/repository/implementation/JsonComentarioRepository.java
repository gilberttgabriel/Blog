package com.software.spring.repository.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.software.spring.model.BaseDeDatos;
import com.software.spring.model.entity.Comentario;
import com.software.spring.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Repository
public class JsonComentarioRepository implements ComentarioRepository {

    private final ObjectMapper mapper;
    private final Path dbPath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonComentarioRepository(
            ObjectMapper injectedMapper,
            @Value("${app.dbPath:./data/db.json}") String configuredPath
    ) {
        this.mapper = injectedMapper.copy()
                .enable(SerializationFeature.INDENT_OUTPUT);

        this.dbPath = Paths.get(configuredPath);
        ensureDbExists();
    }

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

    @Override
    public List<Comentario> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(readDbUnsafe().comentarios);
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo comentarios desde " + dbPath, e);
        } finally {
            lock.readLock().unlock();
        }
    }


    @Override
    public List<Comentario> findByPublicacionId(Integer publicacionId) {
        if (publicacionId == null) return new ArrayList<>();
        return findAll().stream()
                .filter(c -> Objects.equals(c.getPublicacionId(), publicacionId))
                .collect(Collectors.toList());
    }

    @Override
    public Comentario save(Comentario comentario) {
        lock.writeLock().lock();
        try {
            BaseDeDatos db = readDbUnsafe();
            db.comentarios = db.comentarios.stream()
                    .filter(c -> !Objects.equals(c.getId(), comentario.getId()))
                    .collect(Collectors.toCollection(ArrayList::new));
            db.comentarios.add(comentario);
            writeDbUnsafe(db);
            return comentario;
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo comentarios en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) return;
        lock.writeLock().lock();
        try {
            BaseDeDatos db = readDbUnsafe();
            boolean changed = db.comentarios.removeIf(c -> Objects.equals(c.getId(), id));
            if (changed) {
                writeDbUnsafe(db);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error eliminando comentario en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
