package com.document.lawyerfiles.Clases;

public class Casos {


    String tipoCAso;
    String cliente;
    String juex;
    String fecha;
    String etapa;
    String resumen;

    String key;
   // String estado;

    public Casos(){

    }

    public Casos(String tipoCAso, String cliente, String juex, String fecha, String etapa, String resumen, String key) {
        this.tipoCAso = tipoCAso;
        this.cliente = cliente;
        this.juex = juex;
        this.fecha = fecha;
        this.etapa = etapa;
        this.resumen = resumen;
        this.key = key;


    }

    public String getTipoCAso() {
        return tipoCAso;
    }

    public void setTipoCAso(String tipoCAso) {
        this.tipoCAso = tipoCAso;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getJuex() {
        return juex;
    }

    public void setJuex(String juex) {
        this.juex = juex;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
