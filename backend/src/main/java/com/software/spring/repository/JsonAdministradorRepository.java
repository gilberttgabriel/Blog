package com.software.spring.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.software.spring.model.Administrador;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonAdministradorRepository implements AdministradorRepository {
    
    private final Path filePath;
    private final ObjectMapper objectMapper;
    
    public JsonAdministradorRepository() {
        this.filePath = Paths.get("./data/administrador.json");
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ensureFileExists();
    }
    
    private void ensureFileExists() {
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            if (Files.notExists(filePath)) {
                objectMapper.writeValue(filePath.toFile(), new ArrayList<Administrador>());
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar administrador.json en " + filePath, e);
        }
    }
    
    private List<Administrador> readFromFile() {
        try {
            if (Files.size(filePath) == 0L) {
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(filePath.toFile(), new TypeReference<List<Administrador>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    
    @Override
    public Optional<Administrador> findByUsername(String username) {
        return readFromFile().stream()
                .filter(admin -> admin.getUsername().equals(username))
                .findFirst();
    }
}
