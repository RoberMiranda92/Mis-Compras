package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Ticket;

import java.util.List;

/**
 * Created by RobertoMiranda on 17/12/15.
 */
public interface OnLoadComplete {


    public void showError();

    public void loadCompleteCategoria(List<Categoria> items);

    public void loadCompleteTicketProducto(List<LineaProducto> items);

    public void loadCompleteTicket(List<Ticket> items);

    public void getCategories();

    public void onResume();
}
