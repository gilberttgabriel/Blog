package com.software.spring.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mensaje {
    private String contenido;
    private String autorId;  // Cambiado a String para que coincida con Usuario.id (UUID)
    private LocalDateTime fechaCreacion;
    private Integer id;
    private Integer chatId;         // ID del chat al que pertenece el mensaje

    public Mensaje() {}
    // Constructor completo
    public Mensaje(String contenido, String autorId, LocalDateTime fechaCreacion, Integer id, Integer chatId) {
        this.contenido = contenido;
        this.autorId = autorId;
        this.fechaCreacion = fechaCreacion;
        this.id = id;
        this.chatId = chatId;
    }

    // Getters y setters
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getAutorId() { return autorId; }
    public void setAutorId(String autorId) { this.autorId = autorId; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Integer getChatId() { return chatId; }
    public void setChatId(Integer chatId) { this.chatId = chatId; }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
   
}



