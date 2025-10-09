package com.software.spring.model;

import java.time.LocalDateTime;

public class Mensaje {
    private String contenido;
    private String autor;
    private LocalDateTime fechaCreacion;

    // Constructor vacío (necesario para Jackson)
    public Mensaje() {}

    // Constructor completo
    public Mensaje(String contenido, String autor, LocalDateTime fechaCreacion) {
        this.contenido = contenido;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y setters
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return "Mensaje{" +
                "contenido='" + contenido + '\'' +
                ", autor='" + autor + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}



