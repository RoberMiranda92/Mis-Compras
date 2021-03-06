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
import com.ubu.miscompras.model.interactors.TicketGetterByDateInteractor;
import com.ubu.miscompras.model.interactors.TicketGetterByPriceInteractor;
import com.ubu.miscompras.view.fragment.TicketFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Presenter que se encarga de pintar el fragment de Tickets {@link TicketFragment}
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class TicketFragmentPresenter implements IOnLoadComplete {


    private TicketFragment mView;

    /**
     * Contructor de la clase.
     *
     * @param fragment vista asociada al presenter.
     */
    public TicketFragmentPresenter(TicketFragment fragment) {
        this.mView = fragment;

    }

    /**
     * Este método obtiene los tickets entre dos fechas.
     *
     * @param starDate fecha de incio.
     * @param endDate  fecha de fin.
     */
    public void getProductsByDate(Date starDate, Date endDate) {
        if (endDate.before(starDate)) {
            mView.showErrorDate();
        } else {
            TicketGetterByDateInteractor task = new TicketGetterByDateInteractor(this, starDate, endDate);
            task.execute();
        }
    }

    /**
     * Este método obtiene los tickets entre dos importes.
     *
     * @param minPrice importe mínimo.
     * @param maxPrice importe máximo.
     */
    public void getProductsByPrice(String minPrice, String maxPrice) {
        try {
            double min = Double.parseDouble(minPrice);
            double max = Double.parseDouble(maxPrice);

            if (min > max) {
                mView.showPricesError();
            } else {
                TicketGetterByPriceInteractor task = new TicketGetterByPriceInteractor(this, min, max);
                task.execute();
            }
        } catch (NumberFormatException e) {
            mView.showEmptyFieldMessage();
        }

    }

    @Override
    public void getCategories() {
        throw new UnsupportedOperationException("Metodo no soportado");
    }


    @Override
    public void showError() {
    }

    @Override
    public void loadCompleteCategory(List<Category> items) {
        throw new UnsupportedOperationException("Metodo no soportado");
    }

    @Override
    public void loadCompleteLine(List<ProductLine> items) {
        throw new UnsupportedOperationException("Metodo no soportado");
    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {
        if (!items.isEmpty()) {
            mView.setTicket(items);
            mView.showList();
        } else {
            mView.hideList();
            mView.showEmptyListMessage();
        }
    }

    public void setStartDate(Date startDate) {
        mView.setStartDate(startDate);
    }

    public void setEndDate(Date endDate) {
        mView.setEndDate(endDate);
    }


    @Override
    public void onResume() {

        Calendar cal = Calendar.getInstance();
        setEndDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);

        setStartDate(cal.getTime());

    }
}
