package com.ubu.miscompras.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by RobertoMiranda on 8/11/15.
 */
@DatabaseTable(tableName = "ticket")
public class Ticket implements Parcelable {

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


    protected Ticket(Parcel in) {
        id = in.readInt();
        nArticulos = in.readInt();
        total = in.readDouble();
        fecha_compra = new Date(in.readLong());
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (id != ticket.id) return false;
        if (nArticulos != ticket.nArticulos) return false;
        if (Double.compare(ticket.total, total) != 0) return false;
        return fecha_compra.equals(ticket.fecha_compra);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + fecha_compra.hashCode();
        result = 31 * result + nArticulos;
        temp = Double.doubleToLongBits(total);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public int getnArticulos() {
        return nArticulos;
    }

    public void setnArticulos(int nArticulos) {
        this.nArticulos = nArticulos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(nArticulos);
        dest.writeDouble(total);
        dest.writeLong(fecha_compra.getTime());
    }
}
