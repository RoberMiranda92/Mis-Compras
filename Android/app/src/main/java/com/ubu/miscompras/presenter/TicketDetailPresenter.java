package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.ProductGetterByTicketInteractor;
import com.ubu.miscompras.view.activity.TicketDetail;

import java.util.List;

/**
 * Presenter encargado de la vista {@link TicketDetail}
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class TicketDetailPresenter implements IOnLoadComplete {

    private TicketDetail mView;

    /**
     * Constructor d ela clase.
     *
     * @param mView vista asociada.
     */
    public TicketDetailPresenter(TicketDetail mView) {

        this.mView = mView;

    }


    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteCategoria(List<Category> items) {

    }

    @Override
    public void loadCompleteTicketProducto(List<ProductLine> items) {
        mView.setItems(items);
    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {

    }

    @Override
    public void getCategories() {

    }

    public void onResume() {


    }

    /**
     * Este método llama al interactor encargado de obtener las lineas de producto de un ticket.
     *
     * @param ticket ticket.
     */
    public void getTicketLinesByTicket(Ticket ticket) {
        ProductGetterByTicketInteractor task = new ProductGetterByTicketInteractor(this, ticket);
        task.execute();
    }
}
