package com.document.lawyerfiles.Clases;

public class ClsClientes {

    String id_cliente;

    String nombre_cliente;
    String celular_cliente;
    String correo_cliente;
    String direccion_cliente;
    String caracter;

    public ClsClientes(){

    }

    public ClsClientes(String id_cliente, String caracter,  String nombre_cliente, String celular_cliente, String correo_cliente, String direccion_cliente) {
        this.id_cliente = id_cliente;
        this.caracter=caracter;
        this.nombre_cliente = nombre_cliente;
        this.celular_cliente = celular_cliente;
        this.correo_cliente = correo_cliente;
        this.direccion_cliente = direccion_cliente;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getCelular_cliente() {
        return celular_cliente;
    }

    public void setCelular_cliente(String celular_cliente) {
        this.celular_cliente = celular_cliente;
    }

    public String getCorreo_cliente() {
        return correo_cliente;
    }

    public void setCorreo_cliente(String correo_cliente) {
        this.correo_cliente = correo_cliente;
    }

    public String getDireccion_cliente() {
        return direccion_cliente;
    }

    public void setDireccion_cliente(String direccion_cliente) {
        this.direccion_cliente = direccion_cliente;
    }

    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }
}
