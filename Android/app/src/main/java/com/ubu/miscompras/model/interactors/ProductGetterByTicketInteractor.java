package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.IOnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 5/1/16.
 */
public class ProductGetterByTicketInteractor extends AsyncTask<Void, Void, List<ProductLine>> {


    private Ticket ticket;
    private DataBaseHelper db;
    private IOnLoadComplete presenter;
    private Dao<ProductLine, Integer> lineaProductoDao;
    private Dao<Product, Integer> productoDao;

    public ProductGetterByTicketInteractor(IOnLoadComplete presenter, Ticket ticket) {

        this.presenter = presenter;
        this.ticket = ticket;
        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

        try {
            lineaProductoDao = db.getProductLineDAO();
            productoDao = db.getProductDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostExecute(List<ProductLine> items) {

        if (items != null)
            presenter.loadCompleteTicketProducto(items);
        else
            presenter.showError();

        if(db.isOpen())
            db.close();
        OpenHelperManager.releaseHelper();
    }


    @Override
    protected List<ProductLine> doInBackground(Void... params) {

        List<ProductLine> items = new ArrayList<>();
        try {
            GenericRawResults<Object[]> rawResults =
                    lineaProductoDao.queryRaw(
                            "SELECT SUM(" + ProductLine.TABLE_NAME + "." + ProductLine.CANTIDAD + ") AS Cantidad," +
                                    "SUM(" + ProductLine.TABLE_NAME + "." + ProductLine.IMPORTE + ") AS Importe," +
                                    ProductLine.PRECIO + "," + ProductLine.TABLE_NAME + "." + ProductLine.PRODUCTO_ID_FIELD_NAME +
                                    " FROM " + ProductLine.TABLE_NAME +
                                    " WHERE " + ProductLine.TABLE_NAME + "." + ProductLine.TICKET_ID_FIELD_NAME +
                                    " = ? GROUP BY " + ProductLine.TABLE_NAME + "." + ProductLine.PRODUCTO_ID_FIELD_NAME +
                                    "," + ProductLine.TABLE_NAME + "." + ProductLine.PRECIO +
                                    " ORDER BY " + ProductLine.TABLE_NAME + "." + ProductLine.PRODUCTO_ID_FIELD_NAME,
                            new DataType[]{DataType.INTEGER, DataType.DOUBLE, DataType.DOUBLE, DataType.INTEGER}, String.valueOf(ticket.getId()));


            for (Object[] resultArray : rawResults) {
                int cant = (int) resultArray[0];
                double importe = (double) resultArray[1];
                double precio = (double) resultArray[2];
                Product p = productoDao.queryForId((int) resultArray[3]);
                Ticket t = ticket;
                ProductLine linea = new ProductLine(p, cant, precio, importe);
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
