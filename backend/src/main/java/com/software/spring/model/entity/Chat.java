package com.software.spring.model.entity;

import java.util.List;


import java.time.LocalDateTime;

public class Chat {
    private List<Usuario> usuarios;
    private List<Mensaje> mensajes;
    private LocalDateTime fechaCreacion;
    private Integer id;

    public Chat(List<Usuario>usuarios,List<Mensaje> mensajes, LocalDateTime fechaCreacion, Integer id) {
        this.usuarios = usuarios;
        this.mensajes = mensajes;
        this.fechaCreacion = fechaCreacion;
        this.id = id;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    public List<Mensaje> getMensajes() {
        return mensajes;
    }
    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
