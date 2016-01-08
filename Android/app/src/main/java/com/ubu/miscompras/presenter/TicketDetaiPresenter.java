package com.ubu.miscompras.presenter;

import com.ubu.miscompras.view.activity.TicketDetail;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.ProductGetterByTicketInteractor;

import java.util.List;

/**
 * Created by RobertoMiranda on 29/12/15.
 */
public class TicketDetaiPresenter implements  OnLoadComplete{

    private TicketDetail mView;

    public  TicketDetaiPresenter(TicketDetail mView){

        this.mView=mView;

    }


    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteCategoria(List<Categoria> items) {

    }

    @Override
    public void loadCompleteTicketProducto(List<LineaProducto> items) {
        mView.setItems(items);
    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {

    }

    @Override
    public void getCategories() {

    }

    public void onResume(){



    }

    public void getTicketLinesByTicket(Ticket ticket){
        ProductGetterByTicketInteractor task = new ProductGetterByTicketInteractor(this,ticket);
        task.execute();
    }
}
