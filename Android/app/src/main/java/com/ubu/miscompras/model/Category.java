package com.ubu.miscompras.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;

/**
 * Clase que implementa una linea de Producto.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
@DatabaseTable(tableName = "categoria")
public class Category implements Parcelable {

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

    public static final String TABLE_NAME = "categoria";
    public static final String ID_FIELD = "id";
    public static final String NOMBRE_FIELD = "nombre";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private int id;

    @DatabaseField(columnName = NOMBRE_FIELD, unique = true, canBeNull = false)
    private String name;

    @ForeignCollectionField(eager = true)
    private Collection<Product> productos;

    private Category() {

    }

    public Category(String nombre) {
        this.name = nombre;
    }

    protected Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    /**
     * Este método devuelce el id de la categoria.
     *
     * @return id de la categoria.
     */
    public int getId() {
        return id;
    }

    /**
     * Este método establece la id de la categoria.
     *
     * @param id id d ela categoria.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Este método devuelve el nombre de la categoria.
     *
     * @return nombre de la categoria.
     */
    public String getName() {
        return name;
    }

    /**
     * Este método establece el nombre de la categoria.
     *
     * @param name nombre de la categoria.
     */
    public void setName(String name) {
        this.name = name;
    }

    public Collection<Product> getProductos() {
        return productos;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Category)) return false;
        Category otherMyClass = (Category) other;


        if (!otherMyClass.getName().equals(this.getName())) {
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
        dest.writeString(name);
    }
}
