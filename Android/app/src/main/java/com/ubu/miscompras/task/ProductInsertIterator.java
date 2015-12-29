package com.ubu.miscompras.task;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.activity.App;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.AddProductsPresenter;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by RobertoMiranda on 16/12/15.
 */
public class ProductInsertIterator extends AsyncTask<List<LineaProducto>, Void, Boolean> {


    private AddProductsPresenter presenter;
    private DataBaseHelper db;
    private Dao<Producto, Integer> productoDao;
    private Dao<LineaProducto, Integer> lineaProductoDao;
    private Dao<Ticket, Integer> ticketDao;
    private Dao<Categoria, Integer> categoriaDao;

    public ProductInsertIterator(AddProductsPresenter presenter) {
        this.presenter = presenter;
        db = new DataBaseHelper(App.getAppContext());
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
    protected Boolean doInBackground(final List<LineaProducto>... params) {
        final List<LineaProducto> productos = params[0];
        try {
            if (!db.isOpen())
                return false;

            TransactionManager.callInTransaction(db.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Date currentDate = Calendar.getInstance().getTime();
                    Ticket ticket = new Ticket(currentDate);
                    int cant = 0;
                    double total = 0;

                    for (LineaProducto linea : productos) {
                        cant += linea.getCantidad();
                        total += linea.getImporte();
                    }
                    ticket.setTotal(total);
                    ticket.setnArticulos(cant);

                    for (LineaProducto linea : productos) {
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
