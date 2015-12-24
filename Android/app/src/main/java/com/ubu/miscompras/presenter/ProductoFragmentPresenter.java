package com.ubu.miscompras.presenter;

import android.content.Context;

import com.ubu.miscompras.R;
import com.ubu.miscompras.fragment.ProductosFragment;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.TicketProducto;
import com.ubu.miscompras.task.CategoryGetterInteractor;
import com.ubu.miscompras.task.ProductGetterByCategoryIterator;
import com.ubu.miscompras.task.ProductGetterByPriceInteractor;
import com.ubu.miscompras.task.ProductosGetterIteratorByDate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by RobertoMiranda on 17/12/15.
 */
public class ProductoFragmentPresenter implements OnLoadComplete {


    private ProductosFragment mView;

    private boolean animationOn = false;

    public ProductoFragmentPresenter(ProductosFragment productosFragment) {

        this.mView = productosFragment;

    }

    public void getProductosByDate(Date starDate, Date endDate) {
        if (endDate.before(starDate)) {
            mView.showMessage(mView.getString(R.string.errorDates));
        } else {

            ProductosGetterIteratorByDate task = new ProductosGetterIteratorByDate(this, starDate, endDate);
            task.execute();
        }
    }

    public void getProductosByCategoria(Categoria categoria) {
        ProductGetterByCategoryIterator task = new ProductGetterByCategoryIterator(this, categoria);
        task.execute();
    }

    public void getProductosByPrice(String minPrice, String maxPrice) {
        try {
            double min = Double.parseDouble(minPrice);
            double max = Double.parseDouble(maxPrice);

            if (min > max) {
                mView.showMessage(mView.getString(R.string.errorPrices));
            } else {
                ProductGetterByPriceInteractor task = new ProductGetterByPriceInteractor(this, min, max);
                task.execute();
            }
        } catch (NumberFormatException e) {
            mView.showMessage("Los campos no pueden estar vacios");
        }

    }

    public void getCategorias() {
        CategoryGetterInteractor task = new CategoryGetterInteractor(this);
        task.execute();
    }


    @Override
    public void showError() {

    }

    @Override
    public void loadComplete(List items) {

        if (!items.isEmpty()) {
            Object o = items.get(0);
            if (o instanceof Categoria) {
                mView.setCategorias(items);
            }
            if (o instanceof TicketProducto) {
                mView.setItems(items);
            }
            mView.showList();
        } else {
            mView.hideList();
            mView.showMessage(mView.getString(R.string.productsEmpty));
        }

    }

    @Override
    public Context getContext() {
        return mView.getContext();
    }

    public void showStartDialog(Date startDate) {

        mView.showStartDateDialog(startDate);
    }

    public void showEndDateDialog(Date endDate) {
        mView.showEndDateDialog(endDate);
    }

    public void setStartDate(Date startDate) {

        mView.setStartDate(startDate);
    }

    public void setEndDate(Date endDate) {
        mView.setEndDate(endDate);
    }


    public void onResume() {
        getCategorias();
        Calendar cal = Calendar.getInstance();
        setEndDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        setStartDate(cal.getTime());

    }
}
