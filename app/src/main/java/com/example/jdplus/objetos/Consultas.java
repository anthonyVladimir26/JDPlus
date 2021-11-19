package com.example.jdplus.objetos;

public class Consultas {

    String nombreCliente,NombreDoctor,especialidad,fechaHoraCreada,fechaConsulta,horaConsulta,claveUsuario,claveDoctor,tipo,estatus,idConsulta;


    public Consultas(String nombreCliente, String nombreDoctor, String especialidad, String fechaHoraCreada, String fechaConsulta, String horaConsulta, String claveUsuario, String claveDoctor, String tipo, String estatus, String idConsulta) {
        this.nombreCliente = nombreCliente;
        NombreDoctor = nombreDoctor;
        this.especialidad = especialidad;
        this.fechaHoraCreada = fechaHoraCreada;
        this.fechaConsulta = fechaConsulta;
        this.horaConsulta = horaConsulta;
        this.claveUsuario = claveUsuario;
        this.claveDoctor = claveDoctor;
        this.tipo = tipo;
        this.estatus = estatus;
        this.idConsulta = idConsulta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreDoctor() {
        return NombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        NombreDoctor = nombreDoctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getFechaHoraCreada() {
        return fechaHoraCreada;
    }

    public void setFechaHoraCreada(String fechaHoraCreada) {
        this.fechaHoraCreada = fechaHoraCreada;
    }

    public String getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(String fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }

    public String getHoraConsulta() {
        return horaConsulta;
    }

    public void setHoraConsulta(String horaConsulta) {
        this.horaConsulta = horaConsulta;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getClaveDoctor() {
        return claveDoctor;
    }

    public void setClaveDoctor(String claveDoctor) {
        this.claveDoctor = claveDoctor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(String idConsulta) {
        this.idConsulta = idConsulta;
    }
}
