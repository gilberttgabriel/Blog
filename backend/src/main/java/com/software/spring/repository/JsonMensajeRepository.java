package com.software.spring.repository;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.SerializationFeature;
    import com.fasterxml.jackson.core.type.TypeReference;
    import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
    import com.software.spring.model.Mensaje;

import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Repository;
    
    import java.io.IOException;
    import java.nio.file.*;
    import java.util.*;
    import java.util.concurrent.locks.ReentrantReadWriteLock;
    import java.util.stream.Collectors;
    
    @Repository
    public class JsonMensajeRepository implements MensajeRepository {
    
        private final ObjectMapper mapper;
        private final Path dbPath;
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
        public JsonMensajeRepository(
                ObjectMapper injectedMapper,
                @Value("${app.mensajesPath:./data/mensajes.json}") String configuredPath
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
                    writeMensajesUnsafe(new ArrayList<>());
                }
            } catch (IOException e) {
                throw new RuntimeException("No se pudo inicializar mensajes.json en " + dbPath, e);
            }
        }
    
        private List<Mensaje> readMensajesUnsafe() throws IOException {
            if (Files.size(dbPath) == 0L) {
                List<Mensaje> empty = new ArrayList<>();
                writeMensajesUnsafe(empty);
                return empty;
            }
            return mapper.readValue(dbPath.toFile(), new TypeReference<List<Mensaje>>() {});
        }
    
        private void writeMensajesUnsafe(List<Mensaje> mensajes) throws IOException {
            mapper.writeValue(dbPath.toFile(), mensajes);
        }
    
        @Override
        public List<Mensaje> findAll() {
            lock.readLock().lock();
            try {
                return readMensajesUnsafe().stream()
                        .sorted(Comparator.comparing(Mensaje::getFechaCreacion, 
                                Comparator.nullsLast(Comparator.naturalOrder())))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException("Error leyendo mensajes desde " + dbPath, e);
            } finally {
                lock.readLock().unlock();
            }
        }
    
        @Override
        public Optional<Mensaje> findById(Integer id) {
            if (id == null) return Optional.empty();
            return findAll().stream()
                    .filter(m -> Objects.equals(m.getId(), id))
                    .findFirst();
        }
    
        @Override
        public List<Mensaje> findByChatId(Integer chatId) {
            if (chatId == null) return new ArrayList<>();
            return findAll().stream()
                    .filter(m -> Objects.equals(m.getChatId(), chatId))
                    .collect(Collectors.toList());
        }
    
        @Override
        public void save(Mensaje mensaje) {
            lock.writeLock().lock();
            try {
                List<Mensaje> mensajes = readMensajesUnsafe();
                mensajes.add(mensaje);
                writeMensajesUnsafe(mensajes);
            } catch (IOException e) {
                throw new RuntimeException("Error escribiendo mensajes en " + dbPath, e);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

