package com.software.spring.model.entity;

import java.time.LocalDateTime;

public class Anuncio extends Publicacion {
    private String imagen; // Base64 string
    
    // Constructor vacío
    public Anuncio() {
        super();
    }

    // Constructor completo
    public Anuncio(Integer id, String titulo, String contenido, String autorId, LocalDateTime fechaCreacion, String imagen) {
        super(id, titulo, contenido, autorId, fechaCreacion);
        this.imagen = imagen;
    }

    // Getters y setters
    public String getImagen() {
        return imagen;
    }
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
