package com.software.spring.repository.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.software.spring.model.entity.Comentario;
import com.software.spring.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Repositorio que persiste comentarios en ./data/comentarios.json
 * Estructura del archivo: [ { ...Comentario... }, ... ]
 */
@Repository
public class JsonComentarioRepository implements ComentarioRepository {

    private final ObjectMapper mapper;
    private final Path dbPath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonComentarioRepository(
            ObjectMapper injectedMapper,
            @Value("${app.comentariosPath:./data/comentarios.json}") String configuredPath
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
                writeComentariosUnsafe(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar comentarios.json en " + dbPath, e);
        }
    }

    private List<Comentario> readComentariosUnsafe() throws IOException {
        if (Files.size(dbPath) == 0L) {
            List<Comentario> empty = new ArrayList<>();
            writeComentariosUnsafe(empty);
            return empty;
        }
        return mapper.readValue(dbPath.toFile(), new TypeReference<List<Comentario>>() {});
    }

    private void writeComentariosUnsafe(List<Comentario> comentarios) throws IOException {
        mapper.writeValue(dbPath.toFile(), comentarios);
    }

    @Override
    public List<Comentario> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(readComentariosUnsafe());
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
            List<Comentario> comentarios = readComentariosUnsafe();
            comentarios = comentarios.stream()
                    .filter(c -> !Objects.equals(c.getId(), comentario.getId()))
                    .collect(Collectors.toCollection(ArrayList::new));
            comentarios.add(comentario);
            writeComentariosUnsafe(comentarios);
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
            List<Comentario> comentarios = readComentariosUnsafe();
            boolean changed = comentarios.removeIf(c -> Objects.equals(c.getId(), id));
            if (changed) {
                writeComentariosUnsafe(comentarios);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error eliminando comentario en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
