package com.ubu.miscompras.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by RobertoMiranda on 2/11/15.
 */

@DatabaseTable(tableName = "producto")
public class Producto {

    private Producto() {

    }

    public Producto(String nombre, int cantidad, double precio, Categoria categoria) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.categoria = categoria;
    }

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    @DatabaseField(columnName = "nombre", canBeNull = false)
    private String nombre;

    @DatabaseField(columnName = "cantidad", canBeNull = false, defaultValue = "1")
    private int cantidad;

    @DatabaseField(columnName = "precio", canBeNull = false, defaultValue = "0")
    private double precio;

    @DatabaseField(foreign = true, columnName = "id_categoria", foreignAutoCreate = true, foreignAutoRefresh = true)
    private Categoria categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Producto)) return false;
        Producto otherMyClass = (Producto) other;

        if (!otherMyClass.getNombre().equals(this.getNombre())) {
            return false;
        }
        if (!otherMyClass.getNombre().equals(this.getNombre())) {
            return false;
        }
        if (otherMyClass.getCantidad() != this.getCantidad()) {
            return false;
        }
        if (otherMyClass.getPrecio() != this.getPrecio()) {
            return false;
        }
        if (!otherMyClass.getCategoria().equals(this.getCategoria())) {
            return false;
        }
        if (otherMyClass.getCantidad() != this.getCantidad()) {
            return false;
        }
        return true;
    }
}
