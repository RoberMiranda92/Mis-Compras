package com.ubu.miscompras.model;

import java.util.ArrayList;

/**
 * Created by RobertoMiranda on 2/11/15.
 */
public class Categoria {


    private int id;
    private String Nombre;
    private ArrayList<Producto> productos;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
