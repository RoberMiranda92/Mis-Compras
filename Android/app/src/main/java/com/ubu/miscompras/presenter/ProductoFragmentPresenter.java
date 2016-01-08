package com.ubu.miscompras.presenter;

import com.ubu.miscompras.R;
import com.ubu.miscompras.view.fragment.ProductosFragment;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.CategoryGetterInteractor;
import com.ubu.miscompras.model.interactors.ProductGetterByCategoryInterator;
import com.ubu.miscompras.model.interactors.ProductGetterByPriceInteractor;
import com.ubu.miscompras.model.interactors.ProductGetterByDateInteractor;

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

            ProductGetterByDateInteractor task = new ProductGetterByDateInteractor(this, starDate, endDate);
            task.execute();
        }
    }

    public void getProductosByCategoria(Categoria categoria) {
        ProductGetterByCategoryInterator task = new ProductGetterByCategoryInterator(this, categoria);
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

    @Override
    public void getCategories() {
        CategoryGetterInteractor task = new CategoryGetterInteractor(this);
        task.execute();
    }


    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteCategoria(List<Categoria> items) {
        if (!items.isEmpty()) {
            mView.setCategorias(items);
        } else {
            mView.showMessage(mView.getString(R.string.productsEmpty));
        }

    }

    @Override
    public void loadCompleteTicketProducto(List<LineaProducto> items) {
        if (!items.isEmpty()) {
            mView.setProductLines(items);
            mView.showList();
        } else {
            mView.hideList();
            mView.showMessage(mView.getString(R.string.productsEmpty));
        }

    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {

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


    @Override
    public void onResume() {
        getCategories();
        Calendar cal = Calendar.getInstance();
        setEndDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        setStartDate(cal.getTime());

    }
}
