package com.ubu.miscompras.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Clase que implementa una linea de producto.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
@DatabaseTable(tableName = "lineaproducto")
public class ProductLine implements Parcelable {

    public static final String TABLE_NAME = "lineaproducto";
    public static final String TICKET_ID_FIELD_NAME = "id_ticket";
    public static final String PRODUCTO_ID_FIELD_NAME = "id_producto";
    public static final String CANTIDAD = "cantidad";
    public static final String PRECIO = "precio";
    public static final String IMPORTE = "importe";


    @DatabaseField(generatedId = true, columnName = "id")
    private int id;

    @DatabaseField(foreign = true, columnName = TICKET_ID_FIELD_NAME, foreignAutoCreate = true,
            foreignAutoRefresh = true, canBeNull = false)
    private Ticket ticket;

    @DatabaseField(foreign = true, columnName = PRODUCTO_ID_FIELD_NAME, foreignAutoCreate = true,
            foreignAutoRefresh = true, canBeNull = false)
    private Product product;


    @DatabaseField(columnName = CANTIDAD, canBeNull = false, defaultValue = "1")
    private int amount;

    @DatabaseField(columnName = PRECIO, canBeNull = false, defaultValue = "0")
    private double price;

    @DatabaseField(columnName = IMPORTE, canBeNull = false, defaultValue = "0")
    private double totalImport;

    /**
     * Constructor de la la clase.
     *
     * @param ticket      tique de la linea.
     * @param product     producto de la linea.
     * @param cantidad    cantidad de productos de la linea.
     * @param price       precio d ela linea.
     * @param totalImport importe total de la liena.
     */
    public ProductLine(Ticket ticket, Product product, int cantidad, double price, double totalImport) {
        this.ticket = ticket;
        this.product = product;
        this.setAmount(cantidad);
        this.price = price;
        this.totalImport = totalImport;
    }

    /**
     * Constructor de la la clase.
     *
     * @param product     producto de la linea.
     * @param cantidad    cantidad de productos de la linea.
     * @param price       precio d ela linea.
     * @param totalImport importe total de la liena.
     */
    public ProductLine(Product product, int cantidad, double price, double totalImport) {
        this.product = product;
        this.setAmount(cantidad);
        this.price = price;
        this.totalImport = totalImport;
    }

    private ProductLine() {

    }

    protected ProductLine(Parcel in) {
        id = in.readInt();
        amount = in.readInt();
        price = in.readDouble();
        totalImport = in.readDouble();
    }

    public static final Creator<ProductLine> CREATOR = new Creator<ProductLine>() {
        @Override
        public ProductLine createFromParcel(Parcel in) {
            return new ProductLine(in);
        }

        @Override
        public ProductLine[] newArray(int size) {
            return new ProductLine[size];
        }
    };

    /**
     * Devuelve la id de la linea de producto.
     *
     * @return id de linea de producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece la id de la linea de producto.
     *
     * @param id de linea de producto.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Este método devuelve el tique de la linea de producto.
     *
     * @return tique de la linea de producto.
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Este método establece el tique de la linea de producto.
     *
     * @param ticket tique de la linea de producto.
     */
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    /**
     * Este método devuelve el producto de la linea de producto.
     *
     * @return producto de la linea de producto.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Este método establece el producto de la linea de producto.
     *
     * @param product producto de la linea de producto.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Este método devuelve el importe total d ela linea de producto.
     *
     * @return importe total.
     */
    public double getTotalImport() {
        return totalImport;
    }

    /**
     * Este método establece el importe total d ela linea de producto.
     *
     * @param totalImport importe total.
     */
    public void setTotalImport(double totalImport) {
        this.totalImport = totalImport;
    }

    /**
     * Este método devuelve la cantidad de productos de una linea de producto.
     *
     * @return cantidad de productos.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Este método establece la cantidad de productos de una linea de producto.
     *
     * @param amount cantidad de prroductos de la linea de producto.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Este método devuelve el precio de un producto de la linea de producto.
     *
     * @return precio del producto de la linea de producto.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Este método establece el precio de un producto de la linea de producto.
     *
     * @param price precio del producto de la linea de producto
     */
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(amount);
        dest.writeDouble(price);
        dest.writeDouble(totalImport);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductLine that = (ProductLine) o;

        if (id != that.id) return false;
        if (amount != that.amount) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (Double.compare(that.totalImport, totalImport) != 0) return false;
        if (!ticket.equals(that.ticket)) return false;
        return product.equals(that.product);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + ticket.hashCode();
        result = 31 * result + product.hashCode();
        result = 31 * result + amount;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalImport);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
