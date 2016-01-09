/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubu.miscompras.exceptions;

/**
 *
 * @author Roberto
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
