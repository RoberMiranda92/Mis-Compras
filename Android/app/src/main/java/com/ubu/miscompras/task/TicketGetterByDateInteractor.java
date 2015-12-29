package com.ubu.miscompras.task;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.activity.App;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.OnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RobertoMiranda on 29/12/15.
 */
public class TicketGetterByDateInteractor extends AsyncTask<Void, Void, List<Ticket>> {


    private final Date starDate;
    private final Date endDate;
    private final OnLoadComplete presenter;
    private DataBaseHelper db;
    private Dao<LineaProducto, Integer> lineaProductoDao;
    private Dao<Ticket, Integer> ticketDao;
    private Dao<Producto, Integer> productoDao;

    public TicketGetterByDateInteractor(OnLoadComplete presenter, Date startDate, Date endDate) {
        this.starDate = startDate;
        this.endDate = endDate;
        this.presenter = presenter;

        db = new DataBaseHelper(App.getAppContext());

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

    }


    @Override
    protected List<Ticket> doInBackground(Void... params) {

        List<Ticket> items = new ArrayList<>();
        try {
            QueryBuilder<Ticket, Integer> ticketQb = ticketDao.queryBuilder();

            ticketQb.where().between(Ticket.FECHA_FIELD_NAME, starDate, endDate);
            ticketQb.orderByRaw(Ticket.FECHA_FIELD_NAME + " asc");

            items = ticketQb.query();

        } catch (Exception e) {
            Log.d("Excpetion", e.getMessage());
            return null;
        }

        return items;
    }
}
