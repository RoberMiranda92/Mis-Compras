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

import android.content.Context;
import android.net.Uri;

import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.CategoryGetterInteractor;
import com.ubu.miscompras.model.interactors.ComunicatorService;
import com.ubu.miscompras.model.interactors.ProductGetterByCategoryInterator;
import com.ubu.miscompras.view.fragment.MainFragment;

import java.util.List;

/**
 * Presenter encargado del main fragment {@link MainFragment}
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class MainFragmentPresenter implements IOnLoadComplete {


    private MainFragment mView;

    /**
     * Constructor de la clase.
     *
     * @param mView vista asociada.
     */
    public MainFragmentPresenter(MainFragment mView) {

        this.mView = mView;
    }

    /**
     * Este método ejecuta el interator encargado de la subida del fichero de imagen al servidor.
     *
     * @param imageUri uri de iamgen a subir.
     */
    public void uploadFile(Uri imageUri) {
        ComunicatorService tars = new ComunicatorService(this, imageUri);
        tars.execute();
    }

    @Override
    public void getCategories() {
        CategoryGetterInteractor task = new CategoryGetterInteractor(this);
        task.execute();
    }

    /**
     * Callback al terminar la subida del fichero y obtener el JSON de productos.
     *
     * @param result json de productos.
     */
    public void onFinished(String result) {
        hideProgressBar();
        mView.starAddProductActivity(result);
    }


    @Override
    public void showError() {
       mView.showError("Error");
    }

    @Override
    public void loadCompleteCategory(List<Category> items) {
        mView.setCategorias(items);
    }


    @Override
    public void loadCompleteLine(List<ProductLine> items) {
        mView.drawChart(items);
    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {

    }

    /**
     * Este método comunica a la vista que esconda la barra de progreso.
     */
    public void hideProgressBar() {
        mView.hideProgressBar();
    }

    /**
     * Este método comunica a la vista que muestre la barra de progreso.
     */
    public void showProgresBar() {
        mView.showProgresBar();
    }

    /**
     * Este método coloca en la vista el porcentaje del dialogo de subida.
     */
    public void setProgressPercentage(int percentage) {
        mView.setProgressPercentage(percentage);
    }

    /**
     * Este método coloca en la vista el titulo del dialogo de subida.
     */
    public void setProgressBarTitle(String string) {
        mView.setProgressBarTitle(string);
    }

    /**
     * Este método muestra un mensaje de error en la vista.
     *
     * @param message mensaje a mostrar.
     */
    public void showErrorMensage(String message) {
        mView.showError(message);
        mView.hideProgressBar();
    }

    @Override
    public void onResume() {
        getCategories();
    }

    /**
     * Este método llama al interactor que obtiene las lineas de producto de una categoria.
     *
     * @param category categoria.
     */
    public void drawCharByCategoty(Category category) {
        ProductGetterByCategoryInterator task = new ProductGetterByCategoryInterator(this, category);
        task.execute();
    }

    public Context getContext() {
        return mView.getContext();
    }
}
