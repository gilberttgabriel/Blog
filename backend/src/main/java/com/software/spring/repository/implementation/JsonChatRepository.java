package com.software.spring.repository.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.software.spring.model.entity.Chat;
import com.software.spring.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Repository
public class JsonChatRepository implements ChatRepository {

    private final ObjectMapper mapper;
    private final Path dbPath;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonChatRepository(
            ObjectMapper injectedMapper,
            @Value("${app.chatsPath:./data/chats.json}") String configuredPath
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
                writeChatsUnsafe(new ArrayList<>());
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar chats.json en " + dbPath, e);
        }
    }

    private List<Chat> readChatsUnsafe() throws IOException {
        if (Files.size(dbPath) == 0L) {
            List<Chat> empty = new ArrayList<>();
            writeChatsUnsafe(empty);
            return empty;
        }
        return mapper.readValue(dbPath.toFile(), new TypeReference<List<Chat>>() {});
    }

    private void writeChatsUnsafe(List<Chat> chats) throws IOException {
        mapper.writeValue(dbPath.toFile(), chats);
    }

    @Override
    public List<Chat> findAll() {
        lock.readLock().lock();
        try {
            return readChatsUnsafe().stream()
                    .sorted(Comparator.comparing(Chat::getFechaCreacion, 
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo chats desde " + dbPath, e);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Optional<Chat> findById(Integer id) {
        if (id == null) return Optional.empty();
        return findAll().stream()
                .filter(c -> Objects.equals(c.getId(), id))
                .findFirst();
    }

    @Override
    public List<Chat> findByUsuarioId(String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) return new ArrayList<>();
        return findAll().stream()
                .filter(c -> c.getUsuarioIds() != null && c.getUsuarioIds().contains(usuarioId))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Chat chat) {
        lock.writeLock().lock();
        try {
            List<Chat> chats = readChatsUnsafe();
            chats.add(chat);
            writeChatsUnsafe(chats);
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo chats en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(Chat chat) {
        lock.writeLock().lock();
        try {
            List<Chat> chats = readChatsUnsafe();
            boolean found = false;
            
            for (int i = 0; i < chats.size(); i++) {
                if (Objects.equals(chats.get(i).getId(), chat.getId())) {
                    chats.set(i, chat);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                throw new IllegalArgumentException("No se encontró el chat con ID: " + chat.getId());
            }
            
            writeChatsUnsafe(chats);
        } catch (IOException e) {
            throw new RuntimeException("Error actualizando chat en " + dbPath, e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

