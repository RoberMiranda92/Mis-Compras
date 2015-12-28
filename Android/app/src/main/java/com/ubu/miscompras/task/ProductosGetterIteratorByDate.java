package com.ubu.miscompras.task;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.ubu.miscompras.activity.App;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Producto;
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
    private final OnLoadComplete presenter;
    private DataBaseHelper db;
    private Dao<TicketProducto, Integer> lineaProductoDao;
    private Dao<Ticket, Integer> ticketDao;
    private Dao<Producto, Integer> productoDao;

    public ProductosGetterIteratorByDate(OnLoadComplete presenter, Date startDate, Date endDate) {
        this.starDate = startDate;
        this.endDate = endDate;
        this.presenter = presenter;

        db = new DataBaseHelper(App.getAppContext());

        try {
            ticketDao = db.getTicketDAO();
            lineaProductoDao = db.getTicketProductoDAO();
            productoDao = db.getProductoDAO();

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
            GenericRawResults<Object[]> rawResults =
                    lineaProductoDao.queryRaw(
                            "SELECT SUM(" + TicketProducto.TABLE_NAME + "." + TicketProducto.CANTIDAD + ") AS Cantidad," +
                                    "SUM(" + TicketProducto.TABLE_NAME + "." + TicketProducto.IMPORTE + ") AS Importe," +
                                    TicketProducto.PRECIO + "," + TicketProducto.TABLE_NAME + "." + TicketProducto.PRODUCTO_ID_FIELD_NAME +
                                    "," + Ticket.TABLE_NAME + "." + Ticket.ID_FIELD_NAME +
                                    " FROM " + TicketProducto.TABLE_NAME + " JOIN " + Ticket.TABLE_NAME +
                                    " ON " + TicketProducto.TABLE_NAME + "." + TicketProducto.TICKET_ID_FIELD_NAME +
                                    "=" + Ticket.TABLE_NAME + "." + Ticket.ID_FIELD_NAME +
                                    " WHERE " + Ticket.TABLE_NAME + "." + Ticket.FECHA_FIELD_NAME +
                                    " BETWEEN ? AND ? GROUP BY " + TicketProducto.TABLE_NAME + "." + TicketProducto.PRODUCTO_ID_FIELD_NAME +
                                    " ORDER BY " + Ticket.TABLE_NAME + "." + Ticket.FECHA_FIELD_NAME,
                            new DataType[]{DataType.INTEGER, DataType.DOUBLE, DataType.DOUBLE, DataType.INTEGER, DataType.INTEGER}, String.valueOf(starDate.getTime()), String.valueOf(endDate.getTime()));


            for (Object[] resultArray : rawResults) {
                int cant = (int) resultArray[0];
                double importe = (double) resultArray[1];
                double precio = (double) resultArray[2];
                Producto p = productoDao.queryForId((int) resultArray[3]);
                Ticket t = ticketDao.queryForId((int) resultArray[4]);
                TicketProducto linea = new TicketProducto(p, cant, precio, importe);
                linea.setTicket(t);
                items.add(linea);
            }
            rawResults.close();

        } catch (Exception e) {
            Log.d("Excpetion", e.getMessage());
            return null;
        }

        return items;
    }
}
