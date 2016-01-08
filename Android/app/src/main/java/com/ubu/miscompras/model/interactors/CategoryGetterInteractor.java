package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.presenter.OnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 18/12/15.
 */
public class CategoryGetterInteractor extends AsyncTask<Void, Void, List<Categoria>> {


    private Dao<Categoria, Integer> categoriaDao;
    private DataBaseHelper db;
    private OnLoadComplete presenter;

    public CategoryGetterInteractor(OnLoadComplete presenter) {
        this.presenter = presenter;

        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

        try {
            categoriaDao = db.getCategoriaDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostExecute(List<Categoria> items) {

        if (items != null)
            presenter.loadCompleteCategoria(items);
        else
            presenter.showError();

        if(db.isOpen())
            db.close();
        OpenHelperManager.releaseHelper();
    }


    @Override
    protected List<Categoria> doInBackground(Void... params) {

        List<Categoria> items = new ArrayList<>();
        try {
            QueryBuilder<Categoria,Integer> categoryQb= categoriaDao.queryBuilder();
            items = categoryQb.orderBy(Categoria.NOMBRE_FIELD,true).query();

        } catch (SQLException ex) {
            return null;
        }

        return items;
    }
}
