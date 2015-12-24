package com.ubu.miscompras.task;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.OnFinishedListener;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
        db = new DataBaseHelper(presenter.getContext());

        try {
            categoriaDao = db.getCategoriaDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPostExecute(Boolean result) {

        presenter.onFinished(result);

    }

    @Override
    protected Boolean doInBackground(Void... params) {


        if (!db.isOpen())
            return false;
        try {
            TransactionManager.callInTransaction(db.getConnectionSource(), new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Date currentDate = Calendar.getInstance().getTime();
                    Ticket ticket = new Ticket(currentDate);


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
