package com.software.spring.model.entity;

import java.time.LocalDateTime;

public class Anuncio extends Publicacion{
    private byte[] imagen;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String linkExterno;

    public Anuncio(Integer id, String titulo, String contenido, String autor, LocalDateTime fechaCreacion, byte[] imagen, LocalDateTime fechaInicio, LocalDateTime fechaFin, String linkExterno) {
        super(id, titulo, contenido, autor, fechaCreacion);
        this.imagen = imagen;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.linkExterno = linkExterno;
    }

    public byte[] getImagen() {
        return imagen;
    }
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
    public String getLinkExterno() {
        return linkExterno;
    }
    public void setLinkExterno(String linkExterno) {
        this.linkExterno = linkExterno;
    }
}