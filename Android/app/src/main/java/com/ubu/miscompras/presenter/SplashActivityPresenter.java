package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.CaregoryInsertInteractor;
import com.ubu.miscompras.model.interactors.ProductGetterInteractor;
import com.ubu.miscompras.view.activity.SplashActivity;

import java.util.List;

/**
 * Presenter encargado de la SplashActivity.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class SplashActivityPresenter implements IOnFinishedListener, IOnLoadComplete {


    private SplashActivity mView;

    public SplashActivityPresenter(SplashActivity mView) {

        this.mView = mView;
    }

    public void onResume() {
        ProductGetterInteractor task = new ProductGetterInteractor(this);
        task.execute();
    }

    /**
     * Este método comunica llama al interactor que insterta las categorias en la base de datos.
     *
     * @param categoryList lista de categprias.
     */
    public void insertCategories(List<String> categoryList) {
        CaregoryInsertInteractor task = new CaregoryInsertInteractor(this, categoryList);
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
    public void loadCompleteCategoria(List<Category> items) {

    }

    @Override
    public void loadCompleteTicketProducto(List<ProductLine> items) {
        mView.setTicketProducto(items);
    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {
    }

    @Override
    public void getCategories() {

    }

}
