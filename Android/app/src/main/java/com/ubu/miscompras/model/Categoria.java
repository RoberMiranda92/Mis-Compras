package com.ubu.miscompras.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Created by RobertoMiranda on 2/11/15.
 */
@DatabaseTable(tableName = "Categoria")
public class Categoria {

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;
    @DatabaseField(columnName = "nombre")
    private String nombre;

    public Categoria() {

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

}
