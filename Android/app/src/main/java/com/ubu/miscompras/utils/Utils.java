package com.ubu.miscompras.utils;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Categoria;

/**
 * Created by RobertoMiranda on 20/12/15.
 */
public class Utils {


    public static int getCategoryIcon(int idCategory) {

        switch (idCategory) {
            case Categoria.BEBIDAS_ALCOHOLICAS:
                return R.drawable.ic_alcoholic_drink;
            case Categoria.REFRESCOS:
                return R.drawable.ic_glass;
            case Categoria.COMIDARAPIDA:
                return R.drawable.ic_fast_food;
            case Categoria.ENSALADAS:
                return R.drawable.ic_salad;
            case Categoria.CARNE:
                return R.drawable.ic_meat;
            case Categoria.PESCADO:
                return R.drawable.ic_fish;
            case Categoria.PASTA:
                return R.drawable.ic_pasta;
            case Categoria.PLATOS_CALIENTES:
                return R.drawable.ic_hot;
            case Categoria.RACIONES:
                return R.drawable.ic_racion;
            case Categoria.POSTRES:
                return R.drawable.ic_cake;
            case Categoria.PLATOS_COMBINADOS:
                return R.drawable.ic_meal;
            default:
                return R.drawable.ic_other;


        }

    }
}
