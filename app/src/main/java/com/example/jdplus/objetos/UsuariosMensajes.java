package com.example.jdplus.objetos;

public class UsuariosMensajes {

    String nombre,clave,ultimoMensaje;
    int tipoUltimoMensaje, numeroMensajeNuevo;
    Long horaYfechaUltimoMensaje;



    public UsuariosMensajes(String nombre, String clave, String ultimoMensaje, int tipoUltimoMensaje, Long horaYfechaUltimoMensaje, int numeroMensajeNuevo) {

        this.nombre = nombre;
        this.clave = clave;
        this.ultimoMensaje = ultimoMensaje;
        this.tipoUltimoMensaje = tipoUltimoMensaje;
        this.horaYfechaUltimoMensaje = horaYfechaUltimoMensaje;
        this.numeroMensajeNuevo = numeroMensajeNuevo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public int getTipoUltimoMensaje() {
        return tipoUltimoMensaje;
    }

    public void setTipoUltimoMensaje(int tipoUltimoMensaje) {
        this.tipoUltimoMensaje = tipoUltimoMensaje;
    }

    public Long getHoraYfechaUltimoMensaje() {
        return horaYfechaUltimoMensaje;
    }

    public void setHoraYfechaUltimoMensaje(Long horaYfechaUltimoMensaje) {
        this.horaYfechaUltimoMensaje = horaYfechaUltimoMensaje;
    }

    public int getNumeroMensajeNuevo() {
        return numeroMensajeNuevo;
    }

    public void setNumeroMensajeNuevo(int numeroMensajeNuevo) {
        this.numeroMensajeNuevo = numeroMensajeNuevo;
    }
}

