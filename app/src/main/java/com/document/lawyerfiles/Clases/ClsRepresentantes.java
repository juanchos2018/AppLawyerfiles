package com.document.lawyerfiles.Clases;

public class ClsRepresentantes {

    String tipoDoc;
    String nroDoc;
    String nombres;
    String cargo;

    public ClsRepresentantes(String tipoDoc, String nroDoc, String nombres, String cargo) {
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
        this.nombres = nombres;
        this.cargo = cargo;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
