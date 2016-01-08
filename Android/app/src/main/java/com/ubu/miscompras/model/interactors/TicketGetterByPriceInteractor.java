package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.OnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 29/12/15.
 */
public class TicketGetterByPriceInteractor extends AsyncTask<Void, Void, List<Ticket>> {


    private final double minPrice;
    private final double maxPrice;
    private final OnLoadComplete presenter;
    private DataBaseHelper db;
    private Dao<Ticket, Integer> ticketDao;

    public TicketGetterByPriceInteractor(OnLoadComplete presenter, double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.presenter = presenter;

        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

        try {
            ticketDao = db.getTicketDAO();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPostExecute(List<Ticket> items) {

        if (items != null)
            presenter.loadCompleteTicket(items);
        else
            presenter.showError();

        if(db.isOpen())
            db.close();

        OpenHelperManager.releaseHelper();
    }


    @Override
    protected List<Ticket> doInBackground(Void... params) {

        List<Ticket> items = new ArrayList<>();
        try {
            QueryBuilder<Ticket, Integer> ticketQb = ticketDao.queryBuilder();

            ticketQb.where().between(Ticket.IMPORTE_TOTAL_FIELD_NAME, minPrice, maxPrice);
            ticketQb.orderByRaw(Ticket.FECHA_FIELD_NAME + " asc");

            items = ticketQb.query();

        } catch (Exception e) {
            Log.d("Excpetion", e.getMessage());
            return null;
        }

        return items;
    }
}
