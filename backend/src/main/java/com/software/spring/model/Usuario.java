
package com.software.spring.model;

public class Usuario {
    private String id;
    private String username;
    private String description;
    private String nombre;

    // Constructor completo
    public Usuario(String id, String username, String description, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.username = username;
        this.description = description;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }




    // toString (opcional)
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
