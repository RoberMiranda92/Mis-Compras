package com.ubu.miscompras.task;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
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
 * Created by RobertoMiranda on 17/12/15.
 */
public class ProductosGetterIteratorByDate extends AsyncTask<Void, Void, List<LineaProducto>> {


    private final Date starDate;
    private final Date endDate;
    private final OnLoadComplete presenter;
    private DataBaseHelper db;
    private Dao<LineaProducto, Integer> lineaProductoDao;
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
    public void onPostExecute(List<LineaProducto> items) {

        if (items != null)
            presenter.loadCompleteTicketProducto(items);
        else
            presenter.showError();

    }


    @Override
    protected List<LineaProducto> doInBackground(Void... params) {

        List<LineaProducto> items = new ArrayList<>();
        try {
            GenericRawResults<Object[]> rawResults =
                    lineaProductoDao.queryRaw(
                            "SELECT SUM(" + LineaProducto.TABLE_NAME + "." + LineaProducto.CANTIDAD + ") AS Cantidad," +
                                    "SUM(" + LineaProducto.TABLE_NAME + "." + LineaProducto.IMPORTE + ") AS Importe," +
                                    LineaProducto.PRECIO + "," + LineaProducto.TABLE_NAME + "." + LineaProducto.PRODUCTO_ID_FIELD_NAME +
                                    "," + Ticket.TABLE_NAME + "." + Ticket.ID_FIELD_NAME +
                                    " FROM " + LineaProducto.TABLE_NAME + " JOIN " + Ticket.TABLE_NAME +
                                    " ON " + LineaProducto.TABLE_NAME + "." + LineaProducto.TICKET_ID_FIELD_NAME +
                                    "=" + Ticket.TABLE_NAME + "." + Ticket.ID_FIELD_NAME +
                                    " WHERE " + Ticket.TABLE_NAME + "." + Ticket.FECHA_FIELD_NAME +
                                    " BETWEEN ? AND ? GROUP BY " + LineaProducto.TABLE_NAME + "." + LineaProducto.PRODUCTO_ID_FIELD_NAME +
                                    "," + LineaProducto.TABLE_NAME + "." + LineaProducto.PRECIO +
                                    " ORDER BY " + Ticket.TABLE_NAME + "." + Ticket.FECHA_FIELD_NAME + "," + LineaProducto.TABLE_NAME + "." + LineaProducto.PRODUCTO_ID_FIELD_NAME,
                            new DataType[]{DataType.INTEGER, DataType.DOUBLE, DataType.DOUBLE, DataType.INTEGER, DataType.INTEGER}, String.valueOf(starDate.getTime()), String.valueOf(endDate.getTime()));


            for (Object[] resultArray : rawResults) {
                int cant = (int) resultArray[0];
                double importe = (double) resultArray[1];
                double precio = (double) resultArray[2];
                Producto p = productoDao.queryForId((int) resultArray[3]);
                Ticket t = ticketDao.queryForId((int) resultArray[4]);
                LineaProducto linea = new LineaProducto(p, cant, precio, importe);
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
