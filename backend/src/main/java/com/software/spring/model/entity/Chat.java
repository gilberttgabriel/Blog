package com.software.spring.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Chat {
    private Integer id;
    private List<String> usuarioIds;  // IDs de los usuarios participantes
    private LocalDateTime fechaCreacion;
    private String ultimoMensaje;
    
    // Constructor vacío para Jackson
    public Chat() {}

    public Chat(Integer id, List<String> usuarioIds, LocalDateTime fechaCreacion, String ultimoMensaje) {
        this.id = id;
        this.usuarioIds = usuarioIds;
        this.fechaCreacion = fechaCreacion;
        this.ultimoMensaje = ultimoMensaje;
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public List<String> getUsuarioIds() {
        return usuarioIds;
    }
    
    public void setUsuarioIds(List<String> usuarioIds) {
        this.usuarioIds = usuarioIds;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getUltimoMensaje() {
        return ultimoMensaje;
    }
    
    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }
}
