
package com.software.spring.model.entity;

import java.time.LocalDateTime;

public class Publicacion {
    private Integer id;
    private String titulo;
    private String contenido;
    private String autorId;
    private LocalDateTime fechaCreacion;
 

    // Constructor vacío (necesario para Jackson)
    public Publicacion() {
    }

    // Constructor completo
    public Publicacion(Integer id, String titulo, String contenido, String autorId, LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.autorId = autorId;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getAutorId() { return autorId; }
    public void setAutorId(String autorId) { this.autorId = autorId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // toString (opcional)
    @Override
    public String toString() {
        return "Publicacion{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", contenido='" + contenido + '\'' +
                ", autorId='" + autorId + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
