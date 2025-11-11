package com.software.spring.model.entity;
import java.time.LocalDateTime;
public class Administrador extends Perfil {
    private Integer numeroContrato;
    private LocalDateTime fechaRegistro;

    // Constructor vacío (necesario para Jackson)
    public Administrador() {
        super(null, null, null, null, null, false, null);
    }

    public Administrador(String id, String username, String contraseña, String nombre, String apellido, boolean activo, LocalDateTime ultimoAcceso, Integer numeroContrato, LocalDateTime fechaRegistro) {
        super(id, username, contraseña, nombre, apellido, activo, ultimoAcceso);
        this.numeroContrato = numeroContrato;
        this.fechaRegistro = fechaRegistro;
    }
    // Getters y setters
    public Integer getNumeroContrato() { return numeroContrato; }   
    public void setNumeroContrato(Integer numeroContrato) { this.numeroContrato = numeroContrato; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }   
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro;}
}
