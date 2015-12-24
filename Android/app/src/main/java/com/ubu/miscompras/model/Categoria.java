package com.ubu.miscompras.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Created by RobertoMiranda on 2/11/15.
 */
@DatabaseTable(tableName = "categoria")
public class Categoria implements Parcelable{

    public static final int BEBIDAS_ALCOHOLICAS = 1;
    public static final int REFRESCOS = 2;
    public static final int COMIDARAPIDA = 3;
    public static final int ENSALADAS = 4;
    public static final int CARNE = 5;
    public static final int PESCADO = 6;
    public static final int PASTA = 7;
    public static final int PLATOS_CALIENTES = 8;
    public static final int RACIONES = 9;
    public static final int POSTRES = 10;
    public static final int PLATOS_COMBINADOS = 11;
    public static final int OTROS = 12;


    public static final String ID_FIELD = "id";
    public static final String NOMBRE_FIELD = "nombre";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private int id;

    @DatabaseField(columnName = NOMBRE_FIELD, unique = true, canBeNull = false)
    private String nombre;

    @ForeignCollectionField(eager = true)
    private Collection<Producto> productos;

    private Categoria() {

    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    protected Categoria(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    public static final Creator<Categoria> CREATOR = new Creator<Categoria>() {
        @Override
        public Categoria createFromParcel(Parcel in) {
            return new Categoria(in);
        }

        @Override
        public Categoria[] newArray(int size) {
            return new Categoria[size];
        }
    };

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

    public Collection<Producto> getProductos() {
        return productos;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Categoria)) return false;
        Categoria otherMyClass = (Categoria) other;


        if (!otherMyClass.getNombre().equals(this.getNombre())) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
    }
}
