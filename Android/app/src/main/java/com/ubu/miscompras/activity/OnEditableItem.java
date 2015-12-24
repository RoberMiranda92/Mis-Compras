package com.ubu.miscompras.activity;

import com.ubu.miscompras.model.TicketProducto;

/**
 * Created by RobertoMiranda on 22/12/15.
 */
public interface OnEditableItem {


    void OnEditItem(TicketProducto productLine, int position);

    void showOnEditItemError(String message);
}
