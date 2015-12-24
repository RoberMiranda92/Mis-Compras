package com.ubu.miscompras.task;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.TicketProducto;
import com.ubu.miscompras.presenter.AddProductsPresenter;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by RobertoMiranda on 16/12/15.
 */
public class ProductInsertIterator extends AsyncTask<List<TicketProducto>, Void, Boolean> {


    private AddProductsPresenter presenter;
    private DataBaseHelper db;
    private Dao<Producto, Integer> productoDao;
    private Dao<TicketProducto, Integer> lineaProductoDao;
    private Dao<Ticket, Integer> ticketDao;
    private Dao<Categoria, Integer> categoriaDao;

    public ProductInsertIterator(AddProductsPresenter presenter) {
        this.presenter = presenter;
        db = new DataBaseHelper(presenter.getContext());
        try {
            productoDao = db.getProductoDAO();
            lineaProductoDao = db.getTicketProductoDAO();
            ticketDao = db.getTicketDAO();
            categoriaDao = db.getCategoriaDAO();
        } catch (SQLException e) {
            onPostExecute(false);
        }


    }

    @Override
    protected Boolean doInBackground(final List<TicketProducto>... params) {
        final List<TicketProducto> productos = params[0];
        try {
            if (!db.isOpen())
                return false;

            TransactionManager.callInTransaction(db.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Date currentDate = Calendar.getInstance().getTime();
                    Ticket ticket = new Ticket(currentDate);


                    for (TicketProducto linea : productos) {
                        QueryBuilder<Producto, Integer> productoQb = productoDao.queryBuilder();
                        Producto p = productoQb.where().eq(Producto.NOMBRE_FIELD_NAME, linea.getProducto().getNombre()).queryForFirst();
                        if (p != null)
                            linea.setProducto(p);
                        else {
                            p = linea.getProducto();
                            if (p.getCategoria() == null) {
                                Categoria other = categoriaDao.queryForId(Categoria.OTROS);
                                linea.getProducto().setCategoria(other);
                            }

                        }
                        linea.setTicket(ticket);
                        lineaProductoDao.create(linea);
                    }
                    return null;
                }
            });

            return true;
        } catch (SQLException e) {
            Log.d("Exception", e.getMessage());
            return false;
        } finally {
            db.close();
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        presenter.onFinished(result);
    }
}
