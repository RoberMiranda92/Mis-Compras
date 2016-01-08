package com.ubu.miscompras.presenter;

import com.ubu.miscompras.R;
import com.ubu.miscompras.view.fragment.TicketFragment;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.CategoryGetterInteractor;
import com.ubu.miscompras.model.interactors.TicketGetterByDateInteractor;
import com.ubu.miscompras.model.interactors.TicketGetterByPriceInteractor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by RobertoMiranda on 29/12/15.
 */
public class TicketFragmentPresenter implements OnLoadComplete {


    private TicketFragment mView;

    private boolean animationOn = false;

    public TicketFragmentPresenter(TicketFragment fragment) {

        this.mView = fragment;

    }

    public void getProductosByDate(Date starDate, Date endDate) {
        if (endDate.before(starDate)) {
            mView.showMessage(mView.getString(R.string.errorDates));
        } else {
            TicketGetterByDateInteractor task = new TicketGetterByDateInteractor(this, starDate, endDate);
            task.execute();
        }
    }


    public void getProductosByPrice(String minPrice, String maxPrice) {
        try {
            double min = Double.parseDouble(minPrice);
            double max = Double.parseDouble(maxPrice);

            if (min > max) {
                mView.showMessage(mView.getString(R.string.errorPrices));
            } else {
                TicketGetterByPriceInteractor task = new TicketGetterByPriceInteractor(this, min, max);
                task.execute();
            }
        } catch (NumberFormatException e) {
            mView.showMessage("Los campos no pueden estar vacios");
        }

    }

    @Override
    public void getCategories() {
        CategoryGetterInteractor task = new CategoryGetterInteractor(this);
        task.execute();
    }


    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteCategoria(List<Categoria> items) {

    }

    @Override
    public void loadCompleteTicketProducto(List<LineaProducto> items) {

    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {
        if (!items.isEmpty()) {
            mView.setTicket(items);
            mView.showList();
        } else {
            mView.hideList();
            mView.showMessage(mView.getString(R.string.ticketEmpty));
        }
    }


    public void showStartDialog(Date startDate) {

        mView.showStartDateDialog(startDate);
    }

    public void showEndDateDialog(Date endDate) {
        mView.showEndDateDialog(endDate);
    }

    public void setStartDate(Date startDate) {
        mView.setStartDate(startDate);
    }

    public void setEndDate(Date endDate) {
        mView.setEndDate(endDate);
    }


    @Override
    public void onResume() {
        getCategories();
        Calendar cal = Calendar.getInstance();
        setEndDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        setStartDate(cal.getTime());

    }
}
