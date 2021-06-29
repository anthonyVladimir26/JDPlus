package com.example.jdplus.objetos;

public class Mensajes {
    private int id;
    private String usuarioEnvia;
    private String mensajes;
    private String imagenes;
    private String hora;
    private String dia;
    private int tipo;

    public Mensajes(int id, String usuarioEnvia, String mensajes, String imagenes, String hora, String dia, int tipo) {
        this.id = id;
        this.usuarioEnvia = usuarioEnvia;
        this.mensajes = mensajes;
        this.imagenes = imagenes;
        this.hora = hora;
        this.dia = dia;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuarioEnvia() {
        return usuarioEnvia;
    }

    public void setUsuarioEnvia(String usuarioEnvia) {
        this.usuarioEnvia = usuarioEnvia;
    }

    public String getMensajes() {
        return mensajes;
    }

    public void setMensajes(String mensajes) {
        this.mensajes = mensajes;
    }

    public String getImagenes() {
        return imagenes;
    }

    public void setImagenes(String imagenes) {
        this.imagenes = imagenes;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
