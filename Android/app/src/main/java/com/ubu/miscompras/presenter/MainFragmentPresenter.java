package com.ubu.miscompras.presenter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.ubu.miscompras.activity.AddProductsActivity;
import com.ubu.miscompras.activity.CropActivity;
import com.ubu.miscompras.fragment.MainFragment;
import com.ubu.miscompras.task.ComunicatorService;

/**
 * Created by RobertoMiranda on 16/12/15.
 */
public class MainFragmentPresenter {


    private MainFragment mView;

    public MainFragmentPresenter(MainFragment mView) {

        this.mView = mView;
    }


    public void getProducts(Uri uri) {
        String picturePath = getRealPathFromURI(uri);
        ComunicatorService tars = new ComunicatorService(this);
        tars.execute(picturePath);
    }


    public void onFinished(String result) {
        hideProgressBar();
        Intent i = new Intent();
        i.setClass(mView.getActivity(), AddProductsActivity.class);
        i.putExtra("productos", result);
        mView.startActivity(i);
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        try {
            Cursor cursor = mView.getActivity().getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) {
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx); // Exception raised HERE
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Context getContext() {
        return mView.getContext();
    }

    public void hideProgressBar() {
        mView.hideProgressBar();
    }

    public void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mView.startActivityForResult(galleryIntent, mView.LOAD_IMAGE_GALLERY);

    }

    public void startCameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mView.startActivityForResult(takePicture, mView.LOAD_IMAGE_CAMERA);
    }

    public void startCropActivity(Uri source) {
        Intent intent = new Intent();
        intent.setClass(mView.getActivity(), CropActivity.class);
        intent.setData(source);
        mView.startActivityForResult(intent, mView.CROP_PIC);
    }

    public void showProgresBar() {
        mView.showProgresBar();
    }

    public void setProgressPercentage(int percentage) {
        mView.setProgressPercentage(percentage);
    }

    public void setProgressBarTitle(String string) {
        mView.setProgressBarTitle(string);
    }

    public void showErrorMensage(String s) {
        mView.showError(s);
    }
}
