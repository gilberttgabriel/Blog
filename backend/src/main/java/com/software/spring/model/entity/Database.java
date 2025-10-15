package com.software.spring.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Usuario> usuarios = new ArrayList<>();
    // Puedes agregar otras colecciones si quieres:
    // private List<Publicacion> publicaciones = new ArrayList<>();
    // private List<Comentario> comentarios = new ArrayList<>();
    // etc.

    public List<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(List<Usuario> usuarios) { this.usuarios = usuarios; }
}
