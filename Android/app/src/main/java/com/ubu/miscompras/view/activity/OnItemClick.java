package com.ubu.miscompras.view.activity;

import android.content.Context;
import android.view.View;

/**
 * Created by RobertoMiranda on 20/12/15.
 */
public interface OnItemClick {


    public void showMessage(String message);


    public Context getContext();

    void onItemClick(View v);
}
