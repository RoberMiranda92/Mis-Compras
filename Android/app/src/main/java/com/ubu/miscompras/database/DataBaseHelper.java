package com.ubu.miscompras.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.TicketProducto;

import java.sql.SQLException;
import java.util.List;

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
        super(context, DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Categoria.class);
            TableUtils.createTableIfNotExists(connectionSource, Ticket.class);
            TableUtils.createTableIfNotExists(connectionSource, Producto.class);
            TableUtils.createTableIfNotExists(connectionSource, TicketProducto.class);
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
        if (categoriaDAO == null)
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
    
    /*
     * Convenience methods to build and run our prepared queries.
	 */

    private PreparedQuery<Ticket> ticketsForProductoQuery = null;
    private PreparedQuery<Producto> productosForTicketQuery = null;

    public List<Ticket> lookupTicketsForProducto(Producto producto) throws SQLException {
        if (ticketsForProductoQuery == null) {
            ticketsForProductoQuery = makeTicketsForProductoQuery();
        }
        ticketsForProductoQuery.setArgumentHolderValue(0, producto);
        return ticketDAO.query(ticketsForProductoQuery);
    }

    public List<Producto> lookupProductosForTicket(Ticket ticket) throws SQLException {
        if (productosForTicketQuery == null) {
            productosForTicketQuery = makeProductosForTicketQuery();
        }
        productosForTicketQuery.setArgumentHolderValue(0, ticket);
        return productoDAO.query(productosForTicketQuery);
    }

    /**
     * Build our query for Ticket objects that match a Producto.
     */
    private PreparedQuery<Ticket> makeTicketsForProductoQuery() throws SQLException {
        // build our inner query for ProductoTicket objects
        QueryBuilder<TicketProducto, Integer> productoTicketQb = ticketProductoDAO.queryBuilder();
        // just select the ticket-id field
        productoTicketQb.selectColumns(TicketProducto.TICKET_ID_FIELD_NAME);
        SelectArg productoSelectArg = new SelectArg();
        // you could also just pass in producto1 here
        productoTicketQb.where().eq(TicketProducto.TICKET_ID_FIELD_NAME, productoSelectArg);

        // build our outer query for Ticket objects
        QueryBuilder<Ticket, Integer> ticketQb = ticketDAO.queryBuilder();
        // where the id matches in the ticket-id from the inner query
        ticketQb.where().in(Ticket.ID_FIELD_NAME, productoTicketQb);
        return ticketQb.prepare();
    }

    /**
     * Build our query for Producto objects that match a Ticket
     */
    private PreparedQuery<Producto> makeProductosForTicketQuery() throws SQLException {
        QueryBuilder<TicketProducto, Integer> productoTicketQb = ticketProductoDAO.queryBuilder();
        // this time selecting for the producto-id field
        productoTicketQb.selectColumns(TicketProducto.PRODUCTO_ID_FIELD_NAME);
        SelectArg ticketSelectArg = new SelectArg();
        productoTicketQb.where().eq(TicketProducto.TICKET_ID_FIELD_NAME, ticketSelectArg);

        // build our outer query
        QueryBuilder<Producto, Integer> productoQb = productoDAO.queryBuilder();
        // where the producto-id matches the inner query's producto-id field
        productoQb.where().in(Ticket.ID_FIELD_NAME, productoTicketQb);
        return productoQb.prepare();
    }
}
