package com.example.jdplus.objetos;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String usuario, nombre, fcm_token;

    public Usuario(String usuario, String nombre, String fcm_token) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.fcm_token = fcm_token;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
}
