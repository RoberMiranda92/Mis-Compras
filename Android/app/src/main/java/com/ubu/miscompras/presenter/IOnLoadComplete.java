package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;

import java.util.List;

/**
 * Created by RobertoMiranda on 17/12/15.
 */
public interface IOnLoadComplete {


    public void showError();

    public void loadCompleteCategoria(List<Category> items);

    public void loadCompleteTicketProducto(List<ProductLine> items);

    public void loadCompleteTicket(List<Ticket> items);

    public void getCategories();

    public void onResume();
}
