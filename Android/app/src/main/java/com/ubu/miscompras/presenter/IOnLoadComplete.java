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
package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;

import java.util.List;

/**
 * Interfaz de callback para los presenters que acceden a la base de datos.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public interface IOnLoadComplete {

    /**
     * Muestra un error.
     */
    public void showError();

    /**
     * Carga de categorias completa.
     *
     * @param items lista de categorias
     */
    public void loadCompleteCategory(List<Category> items);

    /**
     * Carga de lineas de producto completa.
     *
     * @param items lista de productos.
     */

    public void loadCompleteLine(List<ProductLine> items);

    /**
     * Carga de tiques completa.
     *
     * @param items lista de tiques.
     */
    public void loadCompleteTicket(List<Ticket> items);

    /**
     * Obtiene las categorias
     */
    public void getCategories();

    public void onResume();
}
