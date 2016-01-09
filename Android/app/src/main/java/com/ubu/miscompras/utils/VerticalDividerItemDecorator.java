package com.ubu.miscompras.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Clase que añade un divisor transparente a un recicler view.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class VerticalDividerItemDecorator extends RecyclerView.ItemDecoration {

    private boolean verticalOrientation = true;
    private int space;

    /**
     * Contructor de la clase
     *
     * @param value               tamaño del divisor.
     * @param verticalOrientation orientación vertial.
     */
    public VerticalDividerItemDecorator(int value, boolean verticalOrientation) {
        this.space = value;
        this.verticalOrientation = verticalOrientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        // skip first item in the list
        if (parent.getChildAdapterPosition(view) != 0) {

            if (verticalOrientation) {

                outRect.set(space, 0, 0, 0);

            } else if (!verticalOrientation) {

                outRect.set(0, space, 0, 0);
            }
        }
    }
}