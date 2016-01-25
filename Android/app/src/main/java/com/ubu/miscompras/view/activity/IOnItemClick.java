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
package com.ubu.miscompras.view.activity;

import android.content.Context;
import android.view.View;

/**
 * Interfaz para obtener las vistas de detalle de un tique de la lista.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public interface IOnItemClick {

    /**
     * Muestra un meansaje
     *
     * @param message mensaje.
     */
    public void showMessage(String message);

    /**
     * Devuelve el contexto de la aplicación
     *
     * @return contexto.
     */
    public Context getContext();

    /**
     * accion de click para un elemento.
     *
     * @param v vista sobre la que se actua.
     */
    void onItemClick(View v);
}
