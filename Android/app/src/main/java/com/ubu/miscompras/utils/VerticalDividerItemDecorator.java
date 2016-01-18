/*
*   Copyright (C) 2015 Roberto Miranda.
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
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