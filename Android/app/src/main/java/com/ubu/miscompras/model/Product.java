package com.ubu.miscompras.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Clase que implementa un producto.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */

@DatabaseTable(tableName = "producto")
public class Product {

    public static final String TABLE_NAME = "producto";
    public static final String ID_FIELD_NAME = "id";
    public static final String NOMBRE_FIELD_NAME = "nombre";
    public static final String CATEGORIA_FIELD__ID = "id_categoria";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int id;

    @DatabaseField(columnName = NOMBRE_FIELD_NAME, canBeNull = false)
    private String name;

    @DatabaseField(foreign = true, columnName = CATEGORIA_FIELD__ID, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Category category;

    private Product() {

    }

    /**
     * Constructor de la clase.
     *
     * @param name nombre del producto.
     */
    public Product(String name) {
        this.name = name;

    }

    /**
     * Constructor de la clase.
     *
     * @param name     nombre del producto.
     * @param category categoria del producto.
     */
    public Product(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    /**
     * Este método devuelve el id del producto.
     *
     * @return id del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Este método establce el id del producto.
     *
     * @param id id del producto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Este método devuelve el nombre del producto.
     *
     * @return nombre del producto.
     */
    public String getName() {
        return name;
    }

    /**
     * Este método establece el nombre del producto.
     *
     * @param name nombre del producto.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Este método establece la categoria del producto.
     *
     * @return categoria del producto.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Este método esablce la categoria del producto.
     *
     * @param category categoria del producto.
     */
    public void setCategory(Category category) {
        this.category = category;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Product)) return false;
        Product otherMyClass = (Product) other;

        if (!otherMyClass.getName().equals(this.getName())) {
            return false;
        }
        if (!otherMyClass.getName().equals(this.getName())) {
            return false;
        }
        if (!otherMyClass.getCategory().equals(this.getCategory())) {
            return false;
        }
        return true;
    }
}
