package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.IOnFinishedListener;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by RobertoMiranda on 16/12/15.
 */
public class ProductInsertIterator extends AsyncTask<Void, Void, Boolean> {


    private IOnFinishedListener presenter;
    private DataBaseHelper db;
    private Dao<Product, Integer> productoDao;
    private Dao<ProductLine, Integer> lineaProductoDao;
    private Dao<Ticket, Integer> ticketDao;
    private Dao<Category, Integer> categoriaDao;
    private List<ProductLine> productos;

    public ProductInsertIterator(IOnFinishedListener presenter, List<ProductLine> productos) {
        this.presenter = presenter;
        this.productos= productos;

        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);
        try {
            productoDao = db.getProductDAO();
            lineaProductoDao = db.getProductLineDAO();
            ticketDao = db.getTicketDAO();
            categoriaDao = db.getCategoryDAO();
        } catch (SQLException e) {
            onPostExecute(false);
        }


    }

    @Override
    protected Boolean doInBackground(Void... params) {
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

                    for (ProductLine linea : productos) {
                        cant += linea.getAmount();
                        total += linea.getTotalImport();
                    }
                    ticket.setTotal(total);
                    ticket.setProductAmount(cant);

                    for (ProductLine linea : productos) {
                        QueryBuilder<Product, Integer> productoQb = productoDao.queryBuilder();
                        Product p = productoQb.where().eq(Product.NOMBRE_FIELD_NAME, linea.getProduct().getName()).queryForFirst();
                        if (p != null)
                            linea.setProduct(p);
                        else {
                            p = linea.getProduct();
                            if (p.getCategory() == null) {
                                Category other = categoriaDao.queryForId(Category.OTROS);
                                linea.getProduct().setCategory(other);
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

        if(db.isOpen())
            db.close();
        OpenHelperManager.releaseHelper();
    }
}
