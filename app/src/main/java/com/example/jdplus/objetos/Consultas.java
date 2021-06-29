package com.example.jdplus.objetos;

public class Consultas {
    private String id;
    private String fecha_hora;
    private String tipo_consulta;
    private String doctor;
    private String fecha_cita;
    private String hora_cita;
    private String genero;
    private String nombre;
    private String fecha_nacimiento;
    private String edad;
    private String tipo_sangre;
    private String telefono;
    private String correo;
    private String enfermedad;
    private String enfermedad_info;
    private String medicacion;
    private String info_medicacion;
    private String pago;
    private String cantidad;
    private String clave;


    public Consultas(String id, String fecha_hora, String tipo_consulta, String doctor, String fecha_cita, String hora_cita, String genero, String nombre, String fecha_nacimiento, String edad, String tipo_sangre, String telefono, String correo, String enfermedad, String enfermedad_info, String medicacion, String info_medicacion, String pago, String cantidad, String clave) {
        //this.id = id;
        this.fecha_hora = fecha_hora;
        this.tipo_consulta = tipo_consulta;
        this.doctor = doctor;
        this.fecha_cita = fecha_cita;
        this.hora_cita = hora_cita;
        this.genero = genero;
        this.nombre = nombre;
        this.fecha_nacimiento = fecha_nacimiento;
        this.edad = edad;
        this.tipo_sangre = tipo_sangre;
        this.telefono = telefono;
        this.correo = correo;
        this.enfermedad = enfermedad;
        this.enfermedad_info = enfermedad_info;
        this.medicacion = medicacion;
        this.info_medicacion = info_medicacion;
        this.pago = pago;
        this.cantidad = cantidad;
        this.clave = clave;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getTipo_consulta() {
        return tipo_consulta;
    }

    public void setTipo_consulta(String tipo_consulta) {
        this.tipo_consulta = tipo_consulta;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getFecha_cita() {
        return fecha_cita;
    }

    public void setFecha_cita(String fecha_cita) {
        this.fecha_cita = fecha_cita;
    }

    public String getHora_cita() {
        return hora_cita;
    }

    public void setHora_cita(String hora_cita) {
        this.hora_cita = hora_cita;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTipo_sangre() {
        return tipo_sangre;
    }

    public void setTipo_sangre(String tipo_sangre) {
        this.tipo_sangre = tipo_sangre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public String getEnfermedad_info() {
        return enfermedad_info;
    }

    public void setEnfermedad_info(String enfermedad_info) {
        this.enfermedad_info = enfermedad_info;
    }

    public String getMedicacion() {
        return medicacion;
    }

    public void setMedicacion(String medicacion) {
        this.medicacion = medicacion;
    }

    public String getInfo_medicacion() {
        return info_medicacion;
    }

    public void setInfo_medicacion(String info_medicacion) {
        this.info_medicacion = info_medicacion;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
