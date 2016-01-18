/*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.ubu.miscompras.exceptions;

/**
 *Clase de Excepciones.
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class MisComprasException extends Exception {

    public static final String FICHERO_ES_NULO = "El fichero no puede ser nulo";
    public static final String NO_SE_ENCONTRARON_PRODUCTOS = "No se econtraron productos en la imágen";
    public static final String FICHEROS_PRODUCTOS_ERROR = "Ficheros de producto invalidos";
    public static final String ERROR_TESSERACT = "Error procesando Texto";

    public MisComprasException() {

    }

    public MisComprasException(String mensaje) {
        super(mensaje);
    }

    public MisComprasException(Throwable cause) {
        super(cause);
    }

    public MisComprasException(String message, Throwable cause) {
        super(message, cause);
    }

    public MisComprasException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
