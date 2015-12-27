package com.ubu.miscompras.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.TicketProducto;
import com.ubu.miscompras.presenter.OnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RobertoMiranda on 17/12/15.
 */
public class ProductosGetterIteratorByDate extends AsyncTask<Void, Void, List<TicketProducto>> {


    private final Date starDate;
    private final Date endDate;
    private final Context context;
    private final OnLoadComplete presenter;
    private DataBaseHelper db;
    private Dao<TicketProducto, Integer> lineaProductoDao;
    private Dao<Ticket, Integer> ticketDao;

    public ProductosGetterIteratorByDate(OnLoadComplete presenter, Date startDate, Date endDate) {
        this.starDate = startDate;
        this.endDate = endDate;
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
            presenter.loadCompleteTicketProducto(items);
        else
            presenter.showError();

    }


    @Override
    protected List<TicketProducto> doInBackground(Void... params) {

        List<TicketProducto> items = new ArrayList<>();
        try {
            QueryBuilder<Ticket, Integer> ticketQb = ticketDao.queryBuilder();
            ticketQb.where().ge(Ticket.FECHA_FIELD_NAME, starDate).and().le(Ticket.FECHA_FIELD_NAME, endDate);
            QueryBuilder<TicketProducto, Integer> lineaPRoductoQb = lineaProductoDao.queryBuilder();

            lineaPRoductoQb.join(ticketQb).orderByRaw(Ticket.FECHA_FIELD_NAME + " asc");
            items = lineaPRoductoQb.query();

        } catch (Exception e) {
            Log.d("Excpetion", e.getMessage());
            return null;
        }

        return items;
    }
}
