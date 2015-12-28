package com.ubu.miscompras.task;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.activity.App;
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
public class ProductGetterByPriceInteractor extends AsyncTask<Void, Void, List<TicketProducto>> {


    private final double minPrice;
    private final double maxPrice;
    private final OnLoadComplete presenter;
    private final DataBaseHelper db;
    private Dao<TicketProducto, Integer> lineaProductoDao;
    private Dao<Producto, Integer> productoDao;

    public ProductGetterByPriceInteractor(OnLoadComplete presenter, double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.presenter = presenter;

        db = new DataBaseHelper(App.getAppContext());

        try {
            productoDao = db.getProductoDAO();
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

            GenericRawResults<Object[]> rawResults =
                    lineaProductoDao.queryRaw(
                            "SELECT SUM(" + TicketProducto.TABLE_NAME + "." + TicketProducto.CANTIDAD + ")AS Cantidad," +
                                    "SUM(" + TicketProducto.TABLE_NAME + "." + TicketProducto.IMPORTE + ")AS Importe," +
                                    TicketProducto.PRECIO + "," + Producto.TABLE_NAME + "." + Producto.ID_FIELD_NAME +
                                    " FROM " + TicketProducto.TABLE_NAME + " JOIN " + Producto.TABLE_NAME +
                                    " ON " + TicketProducto.TABLE_NAME + "." + TicketProducto.PRODUCTO_ID_FIELD_NAME +
                                    "=" + Producto.TABLE_NAME + "." + Producto.ID_FIELD_NAME + " JOIN " + Categoria.TABLE_NAME +
                                    " ON " + Producto.TABLE_NAME + "." + Producto.CATEGORIA_FIELD__ID + "=" + Categoria.TABLE_NAME + "." + Categoria.ID_FIELD +
                                    " WHERE " + TicketProducto.TABLE_NAME + "." + TicketProducto.PRECIO +
                                    " BETWEEN ? AND ? GROUP BY " + TicketProducto.TABLE_NAME + "." + TicketProducto.PRODUCTO_ID_FIELD_NAME +
                                    " ORDER BY "+TicketProducto.PRECIO,
                            new DataType[]{DataType.INTEGER, DataType.DOUBLE, DataType.DOUBLE, DataType.INTEGER}, String.valueOf(minPrice),String.valueOf(maxPrice));


            for (Object[] resultArray : rawResults) {
                int cant = (int) resultArray[0];
                double importe = (double) resultArray[1];
                double precio = (double) resultArray[2];
                Producto p = productoDao.queryForId((int) resultArray[3]);
                TicketProducto t = new TicketProducto(p, cant, precio, importe);
                items.add(t);
            }
            rawResults.close();


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return items;
    }
}
