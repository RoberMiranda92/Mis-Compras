package com.ubu.miscompras.model;

/**
 * Created by RobertoMiranda on 3/11/15.
 */
public class Imagen {

    private String data;
    private String nombre;


    public Imagen(String nombre, String data) {
        this.nombre = nombre;
        this.data = data;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
