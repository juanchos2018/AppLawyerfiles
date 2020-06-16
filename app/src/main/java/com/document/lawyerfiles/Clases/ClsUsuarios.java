package com.document.lawyerfiles.Clases;

public class ClsUsuarios {

    String id_usuario;
    String nombre_usuario;
    String correo_usuario;
    String cargo_usuario;
    String image_usuario;

    public ClsUsuarios(){

    }

    public ClsUsuarios(String id_usuario, String nombre_usuario, String correo_usuario, String cargo_usuario, String image_usuario) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.correo_usuario = correo_usuario;
        this.cargo_usuario = cargo_usuario;
        this.image_usuario = image_usuario;
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

    public String getCargo_usuario() {
        return cargo_usuario;
    }

    public void setCargo_usuario(String cargo_usuario) {
        this.cargo_usuario = cargo_usuario;
    }

    public String getImage_usuario() {
        return image_usuario;
    }

    public void setImage_usuario(String image_usuario) {
        this.image_usuario = image_usuario;
    }
}
