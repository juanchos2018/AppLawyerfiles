package com.document.lawyerfiles.Clases;

public class ClsEnvios {

    String key_envio;
    String id_usuario;
    String nombre_archivo;
    String ruta_archivo;
    String peso_archivo;
    String tipo_documento;
    String tipo_archivo;
    String fecha_archivo;

    public ClsEnvios(){

    }

    public ClsEnvios(String key_envio, String id_usuario, String nombre_archivo, String ruta_archivo, String peso_archivo, String tipo_documento, String tipo_archivo, String fecha_archivo) {
        this.key_envio = key_envio;
        this.id_usuario = id_usuario;
        this.nombre_archivo = nombre_archivo;
        this.ruta_archivo = ruta_archivo;
        this.peso_archivo = peso_archivo;
        this.tipo_documento = tipo_documento;
        this.tipo_archivo = tipo_archivo;
        this.fecha_archivo = fecha_archivo;
    }

    public String getKey_envio() {
        return key_envio;
    }

    public void setKey_envio(String key_envio) {
        this.key_envio = key_envio;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_archivo() {
        return nombre_archivo;
    }

    public void setNombre_archivo(String nombre_archivo) {
        this.nombre_archivo = nombre_archivo;
    }

    public String getRuta_archivo() {
        return ruta_archivo;
    }

    public void setRuta_archivo(String ruta_archivo) {
        this.ruta_archivo = ruta_archivo;
    }

    public String getPeso_archivo() {
        return peso_archivo;
    }

    public void setPeso_archivo(String peso_archivo) {
        this.peso_archivo = peso_archivo;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getTipo_archivo() {
        return tipo_archivo;
    }

    public void setTipo_archivo(String tipo_archivo) {
        this.tipo_archivo = tipo_archivo;
    }

    public String getFecha_archivo() {
        return fecha_archivo;
    }

    public void setFecha_archivo(String fecha_archivo) {
        this.fecha_archivo = fecha_archivo;
    }
}
