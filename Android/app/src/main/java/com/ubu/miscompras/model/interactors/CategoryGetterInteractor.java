package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.presenter.IOnLoadComplete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 18/12/15.
 */
public class CategoryGetterInteractor extends AsyncTask<Void, Void, List<Category>> {


    private Dao<Category, Integer> categoriaDao;
    private DataBaseHelper db;
    private IOnLoadComplete presenter;

    public CategoryGetterInteractor(IOnLoadComplete presenter) {
        this.presenter = presenter;

        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

        try {
            categoriaDao = db.getCategoryDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostExecute(List<Category> items) {

        if (items != null)
            presenter.loadCompleteCategoria(items);
        else
            presenter.showError();

        if(db.isOpen())
            db.close();
        OpenHelperManager.releaseHelper();
    }


    @Override
    protected List<Category> doInBackground(Void... params) {

        List<Category> items = new ArrayList<>();
        try {
            QueryBuilder<Category,Integer> categoryQb= categoriaDao.queryBuilder();
            items = categoryQb.orderBy(Category.NOMBRE_FIELD,true).query();

        } catch (SQLException ex) {
            return null;
        }

        return items;
    }
}
