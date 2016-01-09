package com.ubu.miscompras.view.activity;

import android.content.Context;
import android.view.View;


public interface IOnItemClick {


    public void showMessage(String message);


    public Context getContext();

    void onItemClick(View v);
}
