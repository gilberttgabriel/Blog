package com.software.spring.model;

import com.software.spring.model.entity.Usuario;
import com.software.spring.model.entity.Publicacion;
import com.software.spring.model.entity.Comentario;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo completo de la base de datos JSON.
 * Incluye todas las colecciones para que no se sobrescriban entre sí.
 */
public class BaseDeDatos {
    public List<Usuario> usuarios = new ArrayList<>();
    public List<Publicacion> publicaciones = new ArrayList<>();
    public List<Comentario> comentarios = new ArrayList<>();
}

