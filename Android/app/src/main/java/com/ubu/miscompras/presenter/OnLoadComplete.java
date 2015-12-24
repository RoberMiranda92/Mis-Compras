package com.ubu.miscompras.presenter;

import android.content.Context;

import java.util.List;

/**
 * Created by RobertoMiranda on 17/12/15.
 */
public interface OnLoadComplete<T>  {


    public void showError();

    public void loadComplete(List<T> items);


    public Context getContext();
}
