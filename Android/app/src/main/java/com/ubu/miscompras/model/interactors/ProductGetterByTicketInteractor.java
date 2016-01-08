package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
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
 * Created by RobertoMiranda on 5/1/16.
 */
public class ProductGetterByTicketInteractor extends AsyncTask<Void, Void, List<LineaProducto>> {


    private Ticket ticket;
    private DataBaseHelper db;
    private OnLoadComplete presenter;
    private Dao<LineaProducto, Integer> lineaProductoDao;
    private Dao<Producto, Integer> productoDao;

    public ProductGetterByTicketInteractor(OnLoadComplete presenter, Ticket ticket) {

        this.presenter = presenter;
        this.ticket = ticket;
        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

        try {
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

        if(db.isOpen())
            db.close();
        OpenHelperManager.releaseHelper();
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
                                    " FROM " + LineaProducto.TABLE_NAME +
                                    " WHERE " + LineaProducto.TABLE_NAME + "." + LineaProducto.TICKET_ID_FIELD_NAME +
                                    " = ? GROUP BY " + LineaProducto.TABLE_NAME + "." + LineaProducto.PRODUCTO_ID_FIELD_NAME +
                                    "," + LineaProducto.TABLE_NAME + "." + LineaProducto.PRECIO +
                                    " ORDER BY " + LineaProducto.TABLE_NAME + "." + LineaProducto.PRODUCTO_ID_FIELD_NAME,
                            new DataType[]{DataType.INTEGER, DataType.DOUBLE, DataType.DOUBLE, DataType.INTEGER}, String.valueOf(ticket.getId()));


            for (Object[] resultArray : rawResults) {
                int cant = (int) resultArray[0];
                double importe = (double) resultArray[1];
                double precio = (double) resultArray[2];
                Producto p = productoDao.queryForId((int) resultArray[3]);
                Ticket t = ticket;
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
