package com.ubu.miscompras.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by RobertoMiranda on 6/11/15.
 */
@DatabaseTable(tableName = "ticketproducto")
public class TicketProducto implements Parcelable {

    public static final String TABLE_NAME = "ticketproducto";
    public static final String TICKET_ID_FIELD_NAME = "id_ticket";
    public static final String PRODUCTO_ID_FIELD_NAME = "id_producto";
    public static final String CANTIDAD = "cantidadt";
    public static final String PRECIO = "precio";
    public static final String IMPORTE = "importe";


    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    @DatabaseField(foreign = true, columnName = TICKET_ID_FIELD_NAME, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private Ticket ticket;

    @DatabaseField(foreign = true, columnName = PRODUCTO_ID_FIELD_NAME, foreignAutoCreate = true, foreignAutoRefresh = true, canBeNull = false)
    private Producto producto;


    @DatabaseField(columnName = CANTIDAD, canBeNull = false, defaultValue = "1")
    private int cantidad;

    @DatabaseField(columnName = PRECIO, canBeNull = false, defaultValue = "0")
    private double precio;

    @DatabaseField(columnName = IMPORTE, canBeNull = false, defaultValue = "0")
    private double importe;


    public TicketProducto(Ticket ticket, Producto producto, int cantidad, double precio, double importe) {
        this.ticket = ticket;
        this.producto = producto;
        this.setCantidad(cantidad);
        this.precio = precio;
        this.importe = importe;
    }

    public TicketProducto(Producto producto, int cantidad, double precio, double importe) {
        this.producto = producto;
        this.setCantidad(cantidad);
        this.precio = precio;
        this.importe = importe;
    }

    private TicketProducto() {

    }

    protected TicketProducto(Parcel in) {
        id = in.readInt();
        cantidad = in.readInt();
        precio = in.readDouble();
        importe = in.readDouble();
    }

    public static final Creator<TicketProducto> CREATOR = new Creator<TicketProducto>() {
        @Override
        public TicketProducto createFromParcel(Parcel in) {
            return new TicketProducto(in);
        }

        @Override
        public TicketProducto[] newArray(int size) {
            return new TicketProducto[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(cantidad);
        dest.writeDouble(precio);
        dest.writeDouble(importe);
    }
}
