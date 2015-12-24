package com.ubu.miscompras.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by RobertoMiranda on 2/11/15.
 */

@DatabaseTable(tableName = "producto")
public class Producto {


    public static final String ID_FIELD_NAME = "id";
    public static final String NOMBRE_FIELD_NAME = "nombre";
    public static final String CATEGORIA_FIELD__ID = "id_categoria";

    private Producto() {

    }

    public Producto(String nombre) {
        this.nombre = nombre;

    }

    public Producto(String nombre, Categoria categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(columnName = NOMBRE_FIELD_NAME, canBeNull = false)
    private String nombre;

    @DatabaseField(foreign = true, columnName = CATEGORIA_FIELD__ID, foreignAutoCreate = true, foreignAutoRefresh = true)
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
        if (!otherMyClass.getCategoria().equals(this.getCategoria())) {
            return false;
        }
        return true;
    }
}
