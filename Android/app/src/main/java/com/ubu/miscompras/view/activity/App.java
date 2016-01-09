package com.ubu.miscompras.view.activity;

import android.app.Application;
import android.content.Context;

/**
 * Calse que permite obtener el contexto global de la aplicación.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class App extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    /**
     * Este método devuelve el contexto de la aplciación.
     *
     * @return contexto global.
     */
    public static Context getAppContext() {
        return appContext;
    }
}