package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.presenter.OnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 27/12/15.
 */
public class ProductGetterInteractor extends AsyncTask<Void, Void, List<LineaProducto>> {


    private Dao<LineaProducto, Integer> categoriaDao;
    private DataBaseHelper db;
    private OnLoadComplete presenter;

    public ProductGetterInteractor(OnLoadComplete presenter) {
        this.presenter = presenter;

        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

        try {
            categoriaDao = db.getTicketProductoDAO();
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
            items = categoriaDao.queryForAll();
        } catch (SQLException ex) {
            return null;
        }

        return items;
    }
}
