package com.ubu.miscompras.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.ubu.miscompras.utils.RotateBitMapTask;
import com.ubu.miscompras.view.activity.CropActivity;

/**
 * Este presenter se encarga de la Vista CropAvtivity {@link CropActivity}
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class CropPresenter {

    private CropActivity mView;

    /**
     * Constructor de la clase.
     *
     * @param mView vista asociada.
     */
    public CropPresenter(CropActivity mView) {
        this.mView = mView;

    }

    /**
     * Método que llama al interactor que rota la imagen.
     *
     * @param imageUri uri de la imagen a rotar.
     */
    public void onStart(Uri imageUri) {
        mView.showProgresBar();
        RotateBitMapTask task = new RotateBitMapTask(this, imageUri);
        task.execute();

    }

    /**
     * Callback que coloca el bitmap en la vista , o muestra un error si este es null.
     *
     * @param bitmap bitmpap a colocar.
     */
    public void onFinish(Bitmap bitmap) {
        mView.hideProgressBar();
        if (bitmap != null)
            mView.setBitmap(bitmap);
        else
            mView.showError();
    }

    /**
     * Devuelve el contexto de la aplicación.
     *
     * @return
     */
    public Context getContext() {
        return mView;
    }

}
