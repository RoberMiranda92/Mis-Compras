package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.presenter.IOnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 27/12/15.
 */
public class ProductGetterInteractor extends AsyncTask<Void, Void, List<ProductLine>> {


    private Dao<ProductLine, Integer> categoriaDao;
    private DataBaseHelper db;
    private IOnLoadComplete presenter;

    public ProductGetterInteractor(IOnLoadComplete presenter) {
        this.presenter = presenter;

        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

        try {
            categoriaDao = db.getProductLineDAO();
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
            items = categoriaDao.queryForAll();
        } catch (SQLException ex) {
            return null;
        }

        return items;
    }
}
