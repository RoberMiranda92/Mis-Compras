package com.ubu.miscompras.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RobertoMiranda on 8/11/15.
 */
@DatabaseTable(tableName = "ticket")
public class Ticket {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "fecha_compra", dataType = DataType.DATE_STRING,
            format = "yyyy/MM/dd HH:mm:ss")
    private Date fecha_compra;

    @DatabaseField(columnName = "establecimiento")
    private String establecimiento;

    private List<Producto> productos;

    private Ticket() {

    }

    public Ticket(Date fecha_compra, String establecimiento, List<Producto> productos) {
        this.fecha_compra = fecha_compra;
        this.establecimiento = establecimiento;
        this.productos = productos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        establecimiento = establecimiento;
    }

    public List<Producto> getProductos() {


        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public Date getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(Date fecha_compra) {
        this.fecha_compra = fecha_compra;
    }
}
