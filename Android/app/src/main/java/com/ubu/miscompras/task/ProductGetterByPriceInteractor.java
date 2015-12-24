package com.ubu.miscompras.task;

import android.content.Context;
import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.TicketProducto;
import com.ubu.miscompras.presenter.OnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 18/12/15.
 */
public class ProductGetterByPriceInteractor extends AsyncTask<Void, Void, List<TicketProducto>> {


    private final double minPrice;
    private final double maxPrice;
    private final Context context;
    private final OnLoadComplete presenter;
    private final DataBaseHelper db;
    private Dao<Ticket, Integer> ticketDao;
    private Dao<TicketProducto, Integer> lineaProductoDao;

    public ProductGetterByPriceInteractor(OnLoadComplete presenter, double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.context = presenter.getContext();
        this.presenter = presenter;

        db = new DataBaseHelper(context);

        try {
            ticketDao = db.getTicketDAO();
            lineaProductoDao = db.getTicketProductoDAO();

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
        List<TicketProducto> items = new ArrayList<>();

        try {
            QueryBuilder<TicketProducto, Integer> lineaPRoductoQb = lineaProductoDao.queryBuilder();
            lineaPRoductoQb.where().between(TicketProducto.PRECIO, minPrice, maxPrice);

            lineaPRoductoQb.orderBy(TicketProducto.PRECIO,true);
            items = lineaPRoductoQb.query();


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return items;
    }
}
