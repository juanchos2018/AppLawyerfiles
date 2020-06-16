package com.document.lawyerfiles.Clases;

public class ClsCarpetas {

    String id_carpeta;
    String nombre_carpeta;
    String fecha_creacion;
    String cantidad_archivos;

    public ClsCarpetas(){

    }
    public ClsCarpetas(String id_carpeta, String nombre_carpeta, String fecha_creacion, String cantidad_archivos) {
        this.id_carpeta = id_carpeta;
        this.nombre_carpeta = nombre_carpeta;
        this.fecha_creacion = fecha_creacion;
        this.cantidad_archivos = cantidad_archivos;
    }

    public String getId_carpeta() {
        return id_carpeta;
    }

    public void setId_carpeta(String id_carpeta) {
        this.id_carpeta = id_carpeta;
    }

    public String getNombre_carpeta() {
        return nombre_carpeta;
    }

    public void setNombre_carpeta(String nombre_carpeta) {
        this.nombre_carpeta = nombre_carpeta;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public String getCantidad_archivos() {
        return cantidad_archivos;
    }

    public void setCantidad_archivos(String cantidad_archivos) {
        this.cantidad_archivos = cantidad_archivos;
    }
}
