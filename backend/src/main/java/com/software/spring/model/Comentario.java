package com.software.spring.model;

import java.time.LocalDateTime;
public class Comentario {
    private Integer id;
    private String contenido;
    private String autor;
    private Integer publicacionId;
    private LocalDateTime fechaCreacion;

    // Constructor vacío (necesario para Jackson)
    public Comentario() {
    }

    public Comentario(Integer id, String contenido, String autor, Integer publicacionId, LocalDateTime fechaCreacion) {
        this.id = id;
        this.contenido = contenido;
        this.autor = autor;
        this.publicacionId = publicacionId;
        this.fechaCreacion = fechaCreacion;
    }   

    public Integer getId() {
        return id;
    }   
    public void setId(Integer id) {
        this.id = id;
    }   
    public String getContenido() {
        return contenido;
    }   
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public Integer getPublicacionId() {
        return publicacionId;
    }
    public void setPublicacionId(Integer publicacionId) {
        this.publicacionId = publicacionId;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
