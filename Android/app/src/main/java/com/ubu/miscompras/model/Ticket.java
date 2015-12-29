package com.ubu.miscompras.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by RobertoMiranda on 8/11/15.
 */
@DatabaseTable(tableName = "ticket")
public class Ticket {

    public static final String TABLE_NAME = "ticket";
    public static final String ID_FIELD_NAME = "id";
    public static final String FECHA_FIELD_NAME = "fecha_compra";
    public static final String N_ARTICULOS_FIELD_NAME = "numero_articulos";
    public static final String IMPORTE_TOTAL_FIELD_NAME = "importe_total";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(columnName = FECHA_FIELD_NAME, dataType = DataType.DATE_LONG, canBeNull = false)
    private Date fecha_compra;

    @DatabaseField(columnName = N_ARTICULOS_FIELD_NAME, canBeNull = false, defaultValue = "0")
    private int nArticulos;


    @DatabaseField(columnName = IMPORTE_TOTAL_FIELD_NAME, canBeNull = false, defaultValue = "0")
    private double total;


    private Ticket() {

    }

    public Ticket(Date fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public Ticket(Date fecha_compra, double importe, int nArticulos) {
        this.fecha_compra = fecha_compra;
        this.total = importe;
        this.nArticulos = nArticulos;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(Date fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Ticket)) return false;
        Ticket otherMyClass = (Ticket) other;

        if (!otherMyClass.getFecha_compra().equals(this.getFecha_compra()))
            return false;


        return true;
    }

    public int getnArticulos() {
        return nArticulos;
    }

    public void setnArticulos(int nArticulos) {
        this.nArticulos = nArticulos;
    }
}
