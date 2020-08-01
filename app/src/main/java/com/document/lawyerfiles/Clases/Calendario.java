package com.document.lawyerfiles.Clases;

public class Calendario {

    String id_calendar;
    String fecha;
    String asunto;
    String prioridad;
    String descripcion;


    public  Calendario(){

    }
    public Calendario(String id_calendar, String fecha, String asunto, String prioridad, String descripcion) {
        this.id_calendar = id_calendar;
        this.fecha = fecha;
        this.asunto = asunto;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
    }

    public String getId_calendar() {
        return id_calendar;
    }

    public void setId_calendar(String id_calendar) {
        this.id_calendar = id_calendar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
