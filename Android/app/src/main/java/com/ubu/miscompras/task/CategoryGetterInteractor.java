package com.ubu.miscompras.task;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.database.DataBaseHelper;
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

        db = new DataBaseHelper(presenter.getContext());

        try {
            categoriaDao = db.getCategoriaDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostExecute(List<Categoria> items) {

        if (items != null)
            presenter.loadComplete(items);
        else
            presenter.showError();

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
