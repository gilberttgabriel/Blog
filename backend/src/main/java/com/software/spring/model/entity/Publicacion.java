
package com.software.spring.model.entity;

import java.time.LocalDateTime;

public class Publicacion {
    private Integer id;
    private String titulo;
    private String contenido;
    private Perfil autor;
    private LocalDateTime fechaCreacion;
 

    // Constructor completo
    public Publicacion(Integer id, String titulo, String contenido, Perfil autor, LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public Perfil getAutor() { return autor; }
    public void setAutor(Perfil autor) { this.autor = autor; }

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
