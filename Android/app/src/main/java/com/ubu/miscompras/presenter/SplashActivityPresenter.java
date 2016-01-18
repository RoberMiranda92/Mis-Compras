/*
*   Copyright (C) 2015 Roberto Miranda.
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
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
    public void loadCompleteCategory(List<Category> items) {

    }

    @Override
    public void loadCompleteLine(List<ProductLine> items) {
        mView.setTicketProducto(items);
    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {
    }

    @Override
    public void getCategories() {

    }

}
