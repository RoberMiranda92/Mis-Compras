package com.ubu.miscompras.presenter;

import android.content.Context;

import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.TicketProducto;

import java.util.List;

/**
 * Created by RobertoMiranda on 17/12/15.
 */
public interface OnLoadComplete {


    public void showError();

    public void loadCompleteCategoria(List<Categoria> items);

    public void loadCompleteTicketProducto(List<TicketProducto> items);

    public Context getContext();
}
