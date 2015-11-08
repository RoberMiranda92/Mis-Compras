package com.ubu.miscompras.model;



import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by RobertoMiranda on 2/11/15.
 */

@DatabaseTable(tableName = "Ticket")
public class Ticket {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField()
    private Date fechaCompra;

    @DatabaseField()
    private String establecimiento;

    private ArrayList<Producto> productos;

    public Ticket() {

    }

    public Ticket(Date fechaCompra, String establecimiento, ArrayList<Producto> productos) {
        this.fechaCompra = fechaCompra;
        this.establecimiento = establecimiento;
        this.productos = productos;
    }

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
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        establecimiento = establecimiento;
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }
}
