package com.ubu.miscompras.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.utils.Constans;

import java.sql.SQLException;

/**
 * Clase que conecta con la Base de datos.
 * Contiene los DAO(Data Acces Objects)
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = Constans.DATABASE_NAME;
    private static final int DATABASE_VERSION = 1;

    private Dao<Ticket, Integer> ticketDAO = null;
    private RuntimeExceptionDao<Ticket, Integer> ticketRunTimeDAO = null;

    private Dao<Product, Integer> productDAO = null;
    private RuntimeExceptionDao<Product, Integer> productoRunTimeDAO = null;

    private Dao<Category, Integer> categoryDAO = null;
    private RuntimeExceptionDao<Category, Integer> categoriaRunTimeDAO = null;

    private Dao<ProductLine, Integer> productLineDAO = null;
    private RuntimeExceptionDao<ProductLine, Integer> ticketProductoRunTimeDAO = null;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, Ticket.class);
            TableUtils.createTableIfNotExists(connectionSource, Product.class);
            TableUtils.createTableIfNotExists(connectionSource, ProductLine.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    /**
     * Este método devuelve el DAO asociado a los Tiques
     *
     * @return DAO asociado a los tiques.
     * @throws SQLException error al obtener el DAO
     */
    public Dao<Ticket, Integer> getTicketDAO() throws SQLException {
        if (ticketDAO == null) ticketDAO = getDao(Ticket.class);
        return ticketDAO;
    }


    public RuntimeExceptionDao<Ticket, Integer> getTicketRunTimeDAO() {
        if (ticketRunTimeDAO == null) ticketRunTimeDAO = getRuntimeExceptionDao(Ticket.class);
        return ticketRunTimeDAO;
    }

    /**
     * Este método devuelve el DAO asociado a los Productos
     *
     * @return DAO asociado a los productos.
     * @throws SQLException error al obtener el DAO
     */
    public Dao<Product, Integer> getProductDAO() throws SQLException {
        if (productDAO == null) productDAO = getDao(Product.class);
        return productDAO;
    }


    public RuntimeExceptionDao<Product, Integer> getProductoRunTimeDAO() {
        if (productoRunTimeDAO == null)
            productoRunTimeDAO = getRuntimeExceptionDao(Product.class);
        return productoRunTimeDAO;
    }

    /**
     * Este método devuelve el DAO asociado a las Categorias
     *
     * @return DAO asociado a las Categorias.
     * @throws SQLException error al obtener el DAO
     */
    public Dao<Category, Integer> getCategoryDAO() throws SQLException {
        if (categoryDAO == null)
            categoryDAO = getDao(Category.class);
        return categoryDAO;
    }


    public RuntimeExceptionDao<Category, Integer> getCategoriaRunTimeDAO() {
        if (categoriaRunTimeDAO == null)
            categoriaRunTimeDAO = getRuntimeExceptionDao(Category.class);
        return categoriaRunTimeDAO;
    }

    /**
     * Este método devuelve el DAO asociado a las lineas de producto
     *
     * @return DAO asociado a las lineas de producto.
     * @throws SQLException error al obtener el DAO
     */
    public Dao<ProductLine, Integer> getProductLineDAO() throws SQLException {
        if (productLineDAO == null)
            productLineDAO = getDao(ProductLine.class);
        return productLineDAO;
    }


    public RuntimeExceptionDao<ProductLine, Integer> getTicketProductoRunTimeDAO() {
        if (ticketProductoRunTimeDAO == null)
            ticketProductoRunTimeDAO = getRuntimeExceptionDao(ProductLine.class);
        return ticketProductoRunTimeDAO;
    }

    @Override
    public void close() {
        super.close();
        ticketDAO = null;
        ticketRunTimeDAO = null;
        productDAO = null;
        productoRunTimeDAO = null;
        categoryDAO = null;
        categoriaRunTimeDAO = null;
        productLineDAO = null;
        ticketProductoRunTimeDAO = null;


    }
}
