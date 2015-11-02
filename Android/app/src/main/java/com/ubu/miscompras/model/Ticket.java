package com.ubu.miscompras.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by RobertoMiranda on 2/11/15.
 */
public class Ticket {

    private int id;
    private Date fechaCompra;
    private String Establecimiento;
    private ArrayList<Producto> productos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getEstablecimiento() {
        return Establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        Establecimiento = establecimiento;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
