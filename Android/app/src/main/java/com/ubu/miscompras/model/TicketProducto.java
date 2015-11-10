package com.ubu.miscompras.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by RobertoMiranda on 6/11/15.
 */
@DatabaseTable(tableName = "ticketproducto")
public class TicketProducto {


    public static final String TICKET_ID_FIELD_NAME = "id_ticket";
    public static final String PRODUCTO_ID_FIELD_NAME = "id_producto";

    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    @DatabaseField(foreign = true, columnName = TICKET_ID_FIELD_NAME, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Ticket ticket;

    @DatabaseField(foreign = true, columnName = PRODUCTO_ID_FIELD_NAME, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Producto producto;


    public TicketProducto(Ticket ticket, Producto producto) {
        this.ticket = ticket;
        this.producto = producto;
    }

    public TicketProducto() {

    }

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
}
