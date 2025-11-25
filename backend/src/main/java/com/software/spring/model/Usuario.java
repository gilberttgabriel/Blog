
package com.software.spring.model;

import java.time.LocalDateTime;

public class Usuario extends Perfil {
    private String descripcion;
    private Integer edad;

    // Constructor vacío (necesario para Jackson)
    public Usuario() {
        super(null, null, null, null, null, true, null);
    }

    public Usuario(String id, String username, String contraseña, String nombre, String apellido, boolean activo, LocalDateTime ultimoAcceso, String descripcion, Integer edad) {
        super(id, username, contraseña, nombre, apellido, activo, ultimoAcceso);
        this.descripcion = descripcion;
        this.edad = edad;
    }

    // Getters y setters
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }




    // toString (opcional)
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
 