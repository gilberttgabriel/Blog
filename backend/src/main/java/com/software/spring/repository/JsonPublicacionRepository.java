package com.software.spring.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.software.spring.model.Publicacion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
                .registerModule(new JavaTimeModule())
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
                writePublicacionesUnsafe(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar publicaciones.json", e);
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

    @Override
    public List<Publicacion> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(readPublicacionesUnsafe());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo publicaciones", e);
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
        if (titulo == null) return Optional.empty();
        return findAll().stream()
                .filter(p -> Objects.equals(p.getTitulo(), titulo))
                .findFirst();
    }

    @Override
    public void save(Publicacion publicacion) {
        lock.writeLock().lock();
        try {
            List<Publicacion> publicaciones = readPublicacionesUnsafe();
            // save: Agrega una nueva
            publicaciones.add(publicacion);
            writePublicacionesUnsafe(publicaciones);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando publicacion", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(Publicacion publicacion) {
        lock.writeLock().lock();
        try {
            List<Publicacion> publicaciones = readPublicacionesUnsafe();
            // update: Borra la vieja y pone la nueva
            boolean removed = publicaciones.removeIf(p -> Objects.equals(p.getId(), publicacion.getId()));
            if (removed) {
                publicaciones.add(publicacion);
                writePublicacionesUnsafe(publicaciones);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error actualizando publicacion", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(Integer id) {
        if (id == null) return;
        lock.writeLock().lock();
        try {
            List<Publicacion> publicaciones = readPublicacionesUnsafe();
            boolean changed = publicaciones.removeIf(p -> Objects.equals(p.getId(), id));
            if (changed) {
                writePublicacionesUnsafe(publicaciones);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error eliminando publicacion", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}