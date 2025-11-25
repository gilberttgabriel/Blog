package com.software.spring.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.software.spring.model.Anuncio;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonAnuncioRepository implements AnuncioRepository {
    
    private final Path filePath;
    private final ObjectMapper objectMapper;
    
    public JsonAnuncioRepository() {
        this.filePath = Paths.get("./data/anuncios.json");
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
                objectMapper.writeValue(filePath.toFile(), new ArrayList<Anuncio>());
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar anuncios.json en " + filePath, e);
        }
    }
    
    private List<Anuncio> readFromFile() {
        try {
            if (Files.size(filePath) == 0L) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(filePath.toFile(), new TypeReference<List<Anuncio>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private void writeToFile(List<Anuncio> anuncios) {
        try {
            objectMapper.writeValue(filePath.toFile(), anuncios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Anuncio> findAll() {
        return readFromFile();
    }
    
    @Override
    public Optional<Anuncio> findById(Integer id) {
        return readFromFile().stream()
                .filter(anuncio -> anuncio.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Anuncio save(Anuncio anuncio) {
        List<Anuncio> anuncios = readFromFile();
        
        if (anuncio.getId() == null) {
            // Generar nuevo ID
            int maxId = anuncios.stream()
                    .mapToInt(Anuncio::getId)
                    .max()
                    .orElse(0);
            anuncio.setId(maxId + 1);
            anuncios.add(anuncio);
        } else {
            // Actualizar existente
            anuncios.removeIf(a -> a.getId().equals(anuncio.getId()));
            anuncios.add(anuncio);
        }
        
        writeToFile(anuncios);
        return anuncio;
    }
    
    @Override
    public Optional<Anuncio> findLatestActive() {
        // Devuelve el anuncio más reciente por fecha de creación
        return readFromFile().stream()
                .max((a1, a2) -> a1.getFechaCreacion().compareTo(a2.getFechaCreacion()));
    }
}
