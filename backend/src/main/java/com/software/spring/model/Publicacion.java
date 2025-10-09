
package com.software.spring.model;

import java.time.LocalDateTime;

public class Publicacion {
    private String id;
    private String titulo;
    private String contenido;
    private Usuario autor;
    private LocalDateTime fechaCreacion;

    // Constructor vacío
    public Publicacion() {}

    // Constructor completo
    public Publicacion(String id, String titulo, String contenido, Usuario autor, LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // toString (opcional)
    @Override
    public String toString() {
        return "Publicacion{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", contenido='" + contenido + '\'' +
                ", autor=" + (autor != null ? autor.getNombre() : "N/A") +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
