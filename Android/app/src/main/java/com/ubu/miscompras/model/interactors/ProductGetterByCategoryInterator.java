package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.presenter.IOnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 18/12/15.
 */
public class ProductGetterByCategoryInterator extends AsyncTask<Void, Void, List<ProductLine>> {


    private IOnLoadComplete presenter;
    private Dao<ProductLine, Integer> lineaProductoDao;
    private DataBaseHelper db;
    private Category category;
    private Dao<Product, Integer> productoDao;

    public ProductGetterByCategoryInterator(IOnLoadComplete presenter, Category category) {

        this.category = category;
        this.presenter = presenter;

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
        else {
            presenter.showError();
        }

        if(db.isOpen())
            db.close();
        OpenHelperManager.releaseHelper();
    }


    @Override
    protected List<ProductLine> doInBackground(Void... params) {

        List<ProductLine> products = new ArrayList<>();

        try {
            GenericRawResults<Object[]> rawResults =
                    lineaProductoDao.queryRaw(
                            "SELECT SUM(" + ProductLine.TABLE_NAME + "." + ProductLine.CANTIDAD + ")AS Cantidad," +
                                    "SUM(" + ProductLine.TABLE_NAME + "." + ProductLine.IMPORTE + ")AS Importe," +
                                    ProductLine.PRECIO + "," + Product.TABLE_NAME + "." + Product.ID_FIELD_NAME +
                                    " FROM " + ProductLine.TABLE_NAME + " JOIN " + Product.TABLE_NAME +
                                    " ON " + ProductLine.TABLE_NAME + "." + ProductLine.PRODUCTO_ID_FIELD_NAME +
                                    "=" + Product.TABLE_NAME + "." + Product.ID_FIELD_NAME + " JOIN " + Category.TABLE_NAME +
                                    " ON " + Product.TABLE_NAME + "." + Product.CATEGORIA_FIELD__ID + "=" + Category.TABLE_NAME + "." + Category.ID_FIELD +
                                    " WHERE " + Category.TABLE_NAME + "." + Category.ID_FIELD + "=? GROUP BY " + ProductLine.TABLE_NAME + "." + ProductLine.PRODUCTO_ID_FIELD_NAME +
                                    "," + ProductLine.TABLE_NAME + "." + ProductLine.PRECIO +
                                    " ORDER BY " + Product.TABLE_NAME + "." + Product.NOMBRE_FIELD_NAME,
                            new DataType[]{DataType.INTEGER, DataType.DOUBLE, DataType.DOUBLE, DataType.INTEGER}, String.valueOf(category.getId()));


            for (Object[] resultArray : rawResults) {
                int cant = (int) resultArray[0];
                double importe = (double) resultArray[1];
                double precio = (double) resultArray[2];
                Product p = productoDao.queryForId((int) resultArray[3]);
                ProductLine t = new ProductLine(p, cant, precio, importe);
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
