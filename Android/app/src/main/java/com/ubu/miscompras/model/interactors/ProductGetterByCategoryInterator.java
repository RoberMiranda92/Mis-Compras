package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.presenter.OnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 18/12/15.
 */
public class ProductGetterByCategoryInterator extends AsyncTask<Void, Void, List<LineaProducto>> {


    private OnLoadComplete presenter;
    private Dao<LineaProducto, Integer> lineaProductoDao;
    private DataBaseHelper db;
    private Categoria categoria;
    private Dao<Producto, Integer> productoDao;

    public ProductGetterByCategoryInterator(OnLoadComplete presenter, Categoria categoria) {

        this.categoria = categoria;
        this.presenter = presenter;

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

        List<LineaProducto> products = new ArrayList<>();

        try {
            GenericRawResults<Object[]> rawResults =
                    lineaProductoDao.queryRaw(
                            "SELECT SUM(" + LineaProducto.TABLE_NAME + "." + LineaProducto.CANTIDAD + ")AS Cantidad," +
                                    "SUM(" + LineaProducto.TABLE_NAME + "." + LineaProducto.IMPORTE + ")AS Importe," +
                                    LineaProducto.PRECIO + "," + Producto.TABLE_NAME + "." + Producto.ID_FIELD_NAME +
                                    " FROM " + LineaProducto.TABLE_NAME + " JOIN " + Producto.TABLE_NAME +
                                    " ON " + LineaProducto.TABLE_NAME + "." + LineaProducto.PRODUCTO_ID_FIELD_NAME +
                                    "=" + Producto.TABLE_NAME + "." + Producto.ID_FIELD_NAME + " JOIN " + Categoria.TABLE_NAME +
                                    " ON " + Producto.TABLE_NAME + "." + Producto.CATEGORIA_FIELD__ID + "=" + Categoria.TABLE_NAME + "." + Categoria.ID_FIELD +
                                    " WHERE " + Categoria.TABLE_NAME + "." + Categoria.ID_FIELD + "=? GROUP BY " + LineaProducto.TABLE_NAME + "." + LineaProducto.PRODUCTO_ID_FIELD_NAME +
                                    "," + LineaProducto.TABLE_NAME + "." + LineaProducto.PRECIO +
                                    " ORDER BY " + Producto.TABLE_NAME + "." + Producto.NOMBRE_FIELD_NAME,
                            new DataType[]{DataType.INTEGER, DataType.DOUBLE, DataType.DOUBLE, DataType.INTEGER}, String.valueOf(categoria.getId()));


            for (Object[] resultArray : rawResults) {
                int cant = (int) resultArray[0];
                double importe = (double) resultArray[1];
                double precio = (double) resultArray[2];
                Producto p = productoDao.queryForId((int) resultArray[3]);
                LineaProducto t = new LineaProducto(p, cant, precio, importe);
                products.add(t);
            }
            rawResults.close();


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


        return products;
    }
}
