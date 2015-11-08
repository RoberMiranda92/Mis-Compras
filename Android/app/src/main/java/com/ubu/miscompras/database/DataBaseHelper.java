package com.ubu.miscompras.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.TicketProducto;

import java.sql.SQLException;

/**
 * Created by RobertoMiranda on 6/11/15.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASENAME = "db.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Dao<Ticket, Integer> ticketDAO = null;
    private RuntimeExceptionDao<Ticket, Integer> ticketRunTimeDAO = null;

    private Dao<Producto, Integer> productoDAO = null;
    private RuntimeExceptionDao<Producto, Integer> productoRunTimeDAO = null;

    private Dao<Categoria, Integer> categoriaDAO = null;
    private RuntimeExceptionDao<Categoria, Integer> categoriaRunTimeDAO = null;

    private Dao<TicketProducto, Integer> ticketProductoDAO = null;
    private RuntimeExceptionDao<TicketProducto, Integer> ticketProductoRunTimeDAO = null;


    public DataBaseHelper(Context context) {

        super(context, DATABASENAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<Ticket, Integer> getTicketDAO() throws SQLException {
        if (ticketDAO == null) ticketDAO = getDao(Ticket.class);
        return ticketDAO;
    }


    public RuntimeExceptionDao<Ticket, Integer> getTicketRunTimeDAO() {
        if (ticketRunTimeDAO == null) ticketRunTimeDAO = getRuntimeExceptionDao(Ticket.class);
        return ticketRunTimeDAO;
    }


    public Dao<Producto, Integer> getProductoDAO() throws SQLException {
        if (productoDAO == null) productoDAO = getDao(Producto.class);
        return productoDAO;
    }


    public RuntimeExceptionDao<Producto, Integer> getProductoRunTimeDAO() {
        if (productoRunTimeDAO == null)
            productoRunTimeDAO = getRuntimeExceptionDao(Producto.class);
        return productoRunTimeDAO;
    }


    public Dao<Categoria, Integer> getCategoriaDAO() throws SQLException {
        if (categoriaDAO  == null)
            categoriaDAO = getDao(Categoria.class);
        return categoriaDAO;
    }

    public void setCategoriaDAO(Dao<Categoria, Integer> categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public RuntimeExceptionDao<Categoria, Integer> getCategoriaRunTimeDAO() {
        if (categoriaRunTimeDAO == null)
            categoriaRunTimeDAO = getRuntimeExceptionDao(Categoria.class);
        return categoriaRunTimeDAO;
    }


    public Dao<TicketProducto, Integer> getTicketProductoDAO() throws SQLException {
        if (ticketProductoDAO == null)
            ticketProductoDAO = getDao(TicketProducto.class);
        return ticketProductoDAO;
    }


    public RuntimeExceptionDao<TicketProducto, Integer> getTicketProductoRunTimeDAO() {
        if (ticketProductoRunTimeDAO == null)
            ticketProductoRunTimeDAO = getRuntimeExceptionDao(TicketProducto.class);
        return ticketProductoRunTimeDAO;
    }

    @Override
    public void close() {
        super.close();
        ticketDAO = null;
        ticketRunTimeDAO = null;
        productoDAO = null;
        productoRunTimeDAO = null;
        categoriaDAO = null;
        categoriaRunTimeDAO = null;
        ticketProductoDAO = null;
        ticketProductoRunTimeDAO = null;


    }

}
