package com.example.jdplus.objetos;

public class Mensaje {
    private String  mensaje, claveEnvia,imagenUrl ;
    private Long hora;
    private int tipo;

    public Mensaje(String mensaje, String claveEnvia, String imagenUrl, Long hora, int tipo) {
        this.mensaje = mensaje;
        this.claveEnvia = claveEnvia;
        this.imagenUrl =imagenUrl;
        this.hora = hora;
        this.tipo = tipo;

    }



    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getClaveEnvia() {
        return claveEnvia;
    }

    public void setClaveEnvia(String claveEnvia) {
        this.claveEnvia = claveEnvia;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setimagenUrl(String imagenUri) {
        this.imagenUrl = imagenUri;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }


}
