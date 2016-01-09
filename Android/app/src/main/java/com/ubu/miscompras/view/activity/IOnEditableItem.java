package com.ubu.miscompras.view.activity;

import com.ubu.miscompras.model.ProductLine;

/**
 * Interfaz que permite editar las lineas de producto.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public interface IOnEditableItem {


    void OnEditItem(ProductLine productLine, int position);

    void showOnEditItemError(String message);
}
