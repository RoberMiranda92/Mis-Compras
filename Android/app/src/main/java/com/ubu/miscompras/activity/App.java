package com.ubu.miscompras.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by RobertoMiranda on 28/12/15.
 */
public class App extends Application {
    private static Context appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }
    public static Context getAppContext() {
        return appContext;
    }
}