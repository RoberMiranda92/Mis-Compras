package com.ubu.miscompras.view.activity;

import com.ubu.miscompras.model.LineaProducto;

/**
 * Created by RobertoMiranda on 22/12/15.
 */
public interface OnEditableItem {


    void OnEditItem(LineaProducto productLine, int position);

    void showOnEditItemError(String message);
}
