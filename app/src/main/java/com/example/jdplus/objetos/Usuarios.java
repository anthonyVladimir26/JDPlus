package com.example.jdplus.objetos;

import java.io.Serializable;

public class Usuarios implements Serializable {
    private String id,clave, correo,nombre,usuario;

    public Usuarios(String id, String clave, String correo, String nombre, String usuario) {
        this.id = id;
        this.clave = clave;
        this.correo = correo;
        this.nombre = nombre;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}