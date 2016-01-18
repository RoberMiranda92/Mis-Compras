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

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Category;

/**
 * Clase de utilidades.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class Utils {

    /**
     * Este método id del icono asociado a una Categoria.
     *
     * @param idCategory id de la categoria.
     * @return id del icono de la categoria.
     */
    public static int getCategoryIcon(int idCategory) {

        switch (idCategory) {
            case Category.BEBIDAS_ALCOHOLICAS:
                return R.drawable.ic_alcoholic_drink;
            case Category.REFRESCOS:
                return R.drawable.ic_glass;
            case Category.COMIDARAPIDA:
                return R.drawable.ic_fast_food;
            case Category.ENSALADAS:
                return R.drawable.ic_salad;
            case Category.CARNE:
                return R.drawable.ic_meat;
            case Category.PESCADO:
                return R.drawable.ic_fish;
            case Category.PASTA:
                return R.drawable.ic_pasta;
            case Category.PLATOS_CALIENTES:
                return R.drawable.ic_hot;
            case Category.RACIONES:
                return R.drawable.ic_racion;
            case Category.POSTRES:
                return R.drawable.ic_cake;
            case Category.PLATOS_COMBINADOS:
                return R.drawable.ic_meal;
            default:
                return R.drawable.ic_other;


        }

    }
}
