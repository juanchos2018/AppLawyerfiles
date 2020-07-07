package com.document.lawyerfiles.Clases;

public class ClsImagenes {
    String descripcion_imagen;
    String ruta_imagen;


    public  ClsImagenes(){

    }

    public ClsImagenes(String descripcion_imagen, String ruta_imagen) {
        this.descripcion_imagen = descripcion_imagen;
        this.ruta_imagen = ruta_imagen;
    }


    public String getDescripcion_imagen() {
        return descripcion_imagen;
    }

    public void setDescripcion_imagen(String descripcion_imagen) {
        this.descripcion_imagen = descripcion_imagen;
    }

    public String getRuta_imagen() {
        return ruta_imagen;
    }

    public void setRuta_imagen(String ruta_imagen) {
        this.ruta_imagen = ruta_imagen;
    }
}
