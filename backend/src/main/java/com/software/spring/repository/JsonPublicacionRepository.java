package com.software.spring.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.software.spring.model.Publicacion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Repositorio que persiste publicaciones en ./data/publicaciones.json
 * Estructura del archivo: [ { ...Publicacion... }, ... ]
 */
@Repository
public class JsonPublicacionRepository implements PublicacionRepository {

    private final ObjectMapper mapper;
    private final Path dbPath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonPublicacionRepository(
            ObjectMapper injectedMapper,
            @Value("${app.publicacionesPath:./data/publicaciones.json}") String configuredPath
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
                writePublicacionesUnsafe(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar publicaciones.json en " + dbPath, e);
        }
    }

    private List<Publicacion> readPublicacionesUnsafe() throws IOException {
        if (Files.size(dbPath) == 0L) {
            List<Publicacion> empty = new ArrayList<>();
            writePublicacionesUnsafe(empty);
            return empty;
        }
        return mapper.readValue(dbPath.toFile(), new TypeReference<List<Publicacion>>() {});
    }

    private void writePublicacionesUnsafe(List<Publicacion> publicaciones) throws IOException {
        mapper.writeValue(dbPath.toFile(), publicaciones);
    }

    /* ==========================
       Implementación Repository
       ========================== */
    @Override
    public List<Publicacion> findAll() {
        lock.readLock().lock();
        try {
            return readPublicacionesUnsafe().stream()
                    .sorted(Comparator.comparing(Publicacion::getFechaCreacion, 
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
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
    public void save(Publicacion publicacion) {
        lock.writeLock().lock();
        try {
            List<Publicacion> publicaciones = readPublicacionesUnsafe();
            publicaciones.add(publicacion);
            writePublicacionesUnsafe(publicaciones);
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo publicaciones en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
