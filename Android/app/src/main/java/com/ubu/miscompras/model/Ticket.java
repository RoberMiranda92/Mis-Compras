/*
*   Copyright (C) 2015 Roberto Miranda.
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.ubu.miscompras.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Clase que implementa un tique.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
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
    private Date purchaseDate;

    @DatabaseField(columnName = N_ARTICULOS_FIELD_NAME, canBeNull = false, defaultValue = "0")
    private int productAmount;


    @DatabaseField(columnName = IMPORTE_TOTAL_FIELD_NAME, canBeNull = false, defaultValue = "0")
    private double total;


    private Ticket() {

    }

    /**
     * Constructor de la calse.
     *
     * @param purchaseDate fecha de compra del tique.
     */
    public Ticket(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Ticket(Date purchaseDate, double importe, int productAmount) {
        this.purchaseDate = purchaseDate;
        this.total = importe;
        this.productAmount = productAmount;
    }


    protected Ticket(Parcel in) {
        id = in.readInt();
        productAmount = in.readInt();
        total = in.readDouble();
        purchaseDate = new Date(in.readLong());
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

    /**
     * Este método devuelve el id del tique.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Este método estalece el id del tique.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Este método devuelve la fecha de compra del tique.
     *
     * @return fecha de compra del tique.
     */
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Este método establece la fecha de compra del tique.
     *
     * @param purchaseDate fecha de compra del tique.
     */
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Este método devuelve el importe total del tique.
     *
     * @return
     */
    public double getTotal() {
        return total;
    }

    /**
     * Este método establece el importe total del tique.
     *
     * @param total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (id != ticket.id) return false;
        if (productAmount != ticket.productAmount) return false;
        if (Double.compare(ticket.total, total) != 0) return false;
        return purchaseDate.equals(ticket.purchaseDate);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + purchaseDate.hashCode();
        result = 31 * result + productAmount;
        temp = Double.doubleToLongBits(total);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(productAmount);
        dest.writeDouble(total);
        dest.writeLong(purchaseDate.getTime());
    }
}
