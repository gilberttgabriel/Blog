package com.software.spring.model.entity;

import java.time.LocalDateTime;

public abstract class Perfil {
    protected String id;
    protected String username;
    protected String contraseña;
    protected String nombre;
    protected String apellido;
    protected boolean activo;
    protected LocalDateTime ultimoAcceso;

    public Perfil(String id, String username, String contraseña, String nombre, String apellido, boolean activo, LocalDateTime ultimoAcceso) {
        this.id = id;
        this.username = username;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = activo;
        this.ultimoAcceso = ultimoAcceso;
    }
    // Getters y setters
    public String getId() { return id; }            
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
    
}

