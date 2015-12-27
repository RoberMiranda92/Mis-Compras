package com.ubu.miscompras.presenter;

import android.content.Context;

import com.ubu.miscompras.activity.SplashActivity;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.TicketProducto;
import com.ubu.miscompras.task.CaregoryInsertInteractor;
import com.ubu.miscompras.task.TicketProductoGetterInteractor;

import java.util.List;

/**
 * Created by RobertoMiranda on 20/12/15.
 */
public class SplashActivityPresenter implements OnFinishedListener, OnLoadComplete {


    private SplashActivity mView;

    public SplashActivityPresenter(SplashActivity mView) {

        this.mView = mView;
    }

    public void onResume() {
        TicketProductoGetterInteractor task = new TicketProductoGetterInteractor(this);
        task.execute();
    }


    public void insertCategories(List<String> categories) {
        CaregoryInsertInteractor task = new CaregoryInsertInteractor(this, categories);
        task.execute();


    }

    @Override
    public void onFinished(Boolean result) {

        if (!result)
            mView.showError();
        else
            mView.showMessage();
    }

    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteCategoria(List<Categoria> items) {

    }

    @Override
    public void loadCompleteTicketProducto(List<TicketProducto> items) {
        mView.setTicketProducto(items);
    }

    @Override
    public Context getContext() {
        return mView;
    }
}
