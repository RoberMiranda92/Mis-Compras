package com.ubu.miscompras.presenter;

import android.content.Context;

/**
 * Created by RobertoMiranda on 16/12/15.
 */
public interface MainPresenter {

    public void onResume();

    public Context getContext();

    public void onItemClicked(int position);
}