package com.ubu.miscompras.task;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.TicketProducto;
import com.ubu.miscompras.presenter.OnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 18/12/15.
 */
public class ProductGetterByCategoryIterator extends AsyncTask<Void, Void, List<TicketProducto>> {


    private OnLoadComplete presenter;
    private Dao<TicketProducto, Integer> lineaProductoDao;
    private DataBaseHelper db;
    private Categoria categoria;
    private Dao<Producto, Integer> productoDao;

    public ProductGetterByCategoryIterator(OnLoadComplete presenter, Categoria categoria) {

        this.categoria = categoria;
        this.presenter = presenter;

        db = new DataBaseHelper(presenter.getContext());

        try {
            lineaProductoDao = db.getTicketProductoDAO();
            productoDao = db.getProductoDAO();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostExecute(List<TicketProducto> items) {

        if (items != null)
            presenter.loadComplete(items);
        else
            presenter.showError();

    }


    @Override
    protected List<TicketProducto> doInBackground(Void... params) {

        List<TicketProducto> products = new ArrayList<>();

        try {
            QueryBuilder<Producto, Integer> productQb = productoDao.queryBuilder();
            productQb.where().eq(Producto.CATEGORIA_FIELD__ID, categoria);

            QueryBuilder<TicketProducto, Integer> lineaPRoductoQb = lineaProductoDao.queryBuilder();

            products = lineaPRoductoQb.join(productQb).query();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


        return products;
    }
}
