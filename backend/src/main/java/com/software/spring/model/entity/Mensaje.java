package com.software.spring.model.entity;

import java.time.LocalDateTime;

public class Mensaje {
    private String contenido;
    private String autor;
    private LocalDateTime fechaCreacion;
    private Integer id;


    // Constructor completo
    public Mensaje(String contenido, String autor, LocalDateTime fechaCreacion, Integer id) {
        this.contenido = contenido;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.id = id;
    }

    // Getters y setters
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Integer id() { return id; }
    public void id(Integer id) {this.id = id; }
    @Override
    public String toString() {
        return "Mensaje{" +
                "contenido='" + contenido + '\'' +
                ", autor='" + autor + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", id=" +
                '}';
    }
}



