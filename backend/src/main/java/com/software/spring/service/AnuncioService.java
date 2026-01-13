package com.software.spring.service;

import com.software.spring.model.Anuncio;
import com.software.spring.repository.AnuncioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnuncioService {
    
    private final AnuncioRepository anuncioRepository;
    
    public AnuncioService(AnuncioRepository anuncioRepository) {
        this.anuncioRepository = anuncioRepository;
    }
    
    /**
     * Crea un nuevo anuncio (reemplaza al anterior como activo)
     */
    public Anuncio crearAnuncio(Anuncio anuncio) {
        // Validaciones
        if (anuncio.getTitulo() == null || anuncio.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        
        if (anuncio.getContenido() == null || anuncio.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido es obligatorio");
        }
        
        // Generar ID y fecha de creación
        Integer id = Math.abs(java.util.UUID.randomUUID().hashCode());
        LocalDateTime fechaCreacion = LocalDateTime.now();
        
        // Crear nuevo anuncio usando el constructor
        Anuncio nuevoAnuncio = new Anuncio(
            id,
            anuncio.getTitulo(),
            anuncio.getContenido(),
            anuncio.getAutorId(),
            fechaCreacion,
            anuncio.getImagen()
        );
        
        return anuncioRepository.save(nuevoAnuncio);
    }
    
    /**
     * Obtiene el anuncio activo más reciente
     */
    public Anuncio obtenerAnuncioActivo() {
        return anuncioRepository.findLatestActive()
                .orElse(null);
    }
    
    /**
     * Obtiene todos los anuncios
     */
    public List<Anuncio> obtenerTodosLosAnuncios() {
        return anuncioRepository.findAll();
    }
    
    /**
     * Obtiene un anuncio por ID
     */
    public java.util.Optional<Anuncio> obtenerAnuncioPorId(Integer id) {
        return anuncioRepository.findById(id);
    }

    /**
     * Edita un anuncio existente
     */
    public Anuncio editarAnuncio(Integer id, Anuncio cambios) {
        Anuncio existente = anuncioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Anuncio no encontrado: " + id));

        if (cambios.getTitulo() != null && !cambios.getTitulo().trim().isEmpty()) {
            existente.setTitulo(cambios.getTitulo());
        }
        if (cambios.getContenido() != null && !cambios.getContenido().trim().isEmpty()) {
            existente.setContenido(cambios.getContenido());
        }
        if (cambios.getImagen() != null && !cambios.getImagen().trim().isEmpty()) {
            existente.setImagen(cambios.getImagen());
        }

        return anuncioRepository.save(existente);
    }

    /**
     * Elimina un anuncio por ID
     */
    public void eliminarAnuncio(Integer id) {
        anuncioRepository.deleteById(id);
    }
}
