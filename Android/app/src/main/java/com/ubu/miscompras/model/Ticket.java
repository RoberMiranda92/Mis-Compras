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

    public static final String ID_FIELD_NAME = "id";
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(columnName = "fecha_compra", dataType = DataType.DATE_STRING,
            format = "yyyy/MM/dd HH:mm:ss",canBeNull = false)
    private Date fecha_compra;

    @DatabaseField(columnName = "establecimiento")
    private String establecimiento;

    private List<Producto> productos;

    private Ticket() {

    }

    public Ticket(Date fecha_compra, String establecimiento) {
        this.fecha_compra = fecha_compra;
        this.establecimiento = establecimiento;
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

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Ticket)) return false;
        Ticket otherMyClass = (Ticket) other;

        if (!otherMyClass.getFecha_compra().equals(this.getFecha_compra()))
            return false;

        if (!otherMyClass.getEstablecimiento().equals(this.getEstablecimiento())) {
            return false;
        }

        return true;
    }
}
