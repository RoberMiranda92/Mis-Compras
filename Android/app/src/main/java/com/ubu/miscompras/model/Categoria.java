package com.ubu.miscompras.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by RobertoMiranda on 2/11/15.
 */
@DatabaseTable(tableName = "categoria")
public class Categoria {

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    @DatabaseField(columnName = "nombre", unique = true, canBeNull = false)
    private String nombre;

    @ForeignCollectionField(eager = true)
    private Collection<Producto> productos;

    private Categoria() {

    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

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

    public Collection<Producto> getProductos() {
        return productos;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Categoria)) return false;
        Categoria otherMyClass = (Categoria) other;


        if (!otherMyClass.getNombre().equals(this.getNombre())) {
            return false;
        }
        return true;
    }

}
