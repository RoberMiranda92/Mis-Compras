package com.ubu.miscompras.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by RobertoMiranda on 6/11/15.
 */
@DatabaseTable(tableName = "ticketproducto")
public class TicketProducto {


    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    @DatabaseField(foreign = true, columnName = "id_ticket")
    private Ticket ticket;

    @DatabaseField(foreign = true, columnName = "id_producto")
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
