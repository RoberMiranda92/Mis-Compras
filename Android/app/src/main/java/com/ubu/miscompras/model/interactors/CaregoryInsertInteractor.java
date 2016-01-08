package com.ubu.miscompras.model.interactors;

import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.presenter.OnFinishedListener;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by RobertoMiranda on 20/12/15.
 */

public class CaregoryInsertInteractor extends AsyncTask<Void, Void, Boolean> {


    private List<String> categories;
    private Dao<Categoria, Integer> categoriaDao;
    private DataBaseHelper db;
    private OnFinishedListener presenter;

    public CaregoryInsertInteractor(OnFinishedListener presenter, List<String> categories) {
        this.presenter = presenter;
        this.categories = categories;
        db = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

        try {
            categoriaDao = db.getCategoriaDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPostExecute(Boolean result) {

        presenter.onFinished(result);

        if(db.isOpen())
            db.close();
        OpenHelperManager.releaseHelper();

    }

    @Override
    protected Boolean doInBackground(Void... params) {


        if (!db.isOpen())
            return false;
        try {
            TransactionManager.callInTransaction(db.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {


                    for (String name : categories) {

                        categoriaDao.create(new Categoria(name));

                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
