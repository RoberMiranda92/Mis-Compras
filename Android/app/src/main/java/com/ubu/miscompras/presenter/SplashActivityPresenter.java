package com.ubu.miscompras.presenter;

import android.content.Context;

import com.ubu.miscompras.activity.SplashActivity;
import com.ubu.miscompras.task.CaregoryInsertInteractor;

import java.util.List;

/**
 * Created by RobertoMiranda on 20/12/15.
 */
public class SplashActivityPresenter implements OnFinishedListener {


    private SplashActivity mView;

    public SplashActivityPresenter(SplashActivity mView) {

        this.mView = mView;
    }


    public void insertCategories(List<String> categories) {
        CaregoryInsertInteractor task = new CaregoryInsertInteractor(this, categories);
        task.execute();


    }

    @Override
    public void onFinished(Boolean result) {

        if (!result)
            mView.showError();
        else
            mView.showMessage();
    }

    @Override
    public Context getContext() {
        return mView;
    }
}
