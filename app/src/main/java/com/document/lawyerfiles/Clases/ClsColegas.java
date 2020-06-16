package com.document.lawyerfiles.Clases;

public class ClsColegas {

    String key_usuario;
    String id_usuario;
    String nombre_usuario;
    String correo_usuario;
    String image_usuario;

    public ClsColegas(){

    }
    public ClsColegas(String key_usuario, String id_usuario, String nombre_usuario, String correo_usuario, String image_usuario) {
        this.key_usuario = key_usuario;
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.correo_usuario = correo_usuario;
        this.image_usuario = image_usuario;
    }

    public String getKey_usuario() {
        return key_usuario;
    }

    public void setKey_usuario(String key_usuario) {
        this.key_usuario = key_usuario;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getImage_usuario() {
        return image_usuario;
    }

    public void setImage_usuario(String image_usuario) {
        this.image_usuario = image_usuario;
    }
}
