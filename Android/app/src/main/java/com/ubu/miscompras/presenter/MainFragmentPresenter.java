package com.ubu.miscompras.presenter;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.ubu.miscompras.fragment.MainFragment;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.task.CategoryGetterInteractor;
import com.ubu.miscompras.task.ComunicatorService;
import com.ubu.miscompras.task.ProductGetterByCategoryIterator;

import java.util.List;

/**
 * Created by RobertoMiranda on 16/12/15.
 */
public class MainFragmentPresenter implements OnLoadComplete {


    private MainFragment mView;

    public MainFragmentPresenter(MainFragment mView) {

        this.mView = mView;
    }


    public void getProducts(Uri uri) {
        String picturePath = getRealPathFromURI(uri);
        ComunicatorService tars = new ComunicatorService(this);
        tars.execute(picturePath);
    }

    @Override
    public void getCategories() {
        CategoryGetterInteractor task = new CategoryGetterInteractor(this);
        task.execute();
    }


    public void onFinished(String result) {
        hideProgressBar();
        mView.starAddProductActivity(result);
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

    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteCategoria(List<Categoria> items) {
        mView.setCategorias(items);
    }


    @Override
    public void loadCompleteTicketProducto(List<LineaProducto> items) {
        mView.setProductLines(items);
    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {

    }


    public void hideProgressBar() {
        mView.hideProgressBar();
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

    @Override
    public void onResume() {
        getCategories();
    }


    public void drawCharByCategoty(Categoria item) {
        ProductGetterByCategoryIterator task = new ProductGetterByCategoryIterator(this, item);
        task.execute();
    }
}
