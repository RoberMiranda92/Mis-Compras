package com.ubu.miscompras.presenter;

import com.ubu.miscompras.view.activity.SplashActivity;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.CaregoryInsertInteractor;
import com.ubu.miscompras.model.interactors.ProductGetterInteractor;

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
        ProductGetterInteractor task = new ProductGetterInteractor(this);
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

        mView.start();
    }

    @Override
    public void showError() {
        mView.showError();
    }

    @Override
    public void loadCompleteCategoria(List<Categoria> items) {

    }

    @Override
    public void loadCompleteTicketProducto(List<LineaProducto> items) {
        mView.setTicketProducto(items);
    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {
    }

    @Override
    public void getCategories() {

    }

}
