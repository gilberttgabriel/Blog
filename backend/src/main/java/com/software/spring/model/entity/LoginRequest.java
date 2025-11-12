package com.software.spring.model.entity;

public class LoginRequest {
    private String username;
    private String contraseña;
    
    // Constructor vacío (necesario para Jackson)
    public LoginRequest() {}
    
    // Constructor con parámetros
    public LoginRequest(String username, String contraseña) {
        this.username = username;
        this.contraseña = contraseña;
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
}

