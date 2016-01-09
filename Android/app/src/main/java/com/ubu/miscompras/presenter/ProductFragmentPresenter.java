package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.CategoryGetterInteractor;
import com.ubu.miscompras.model.interactors.ProductGetterByCategoryInterator;
import com.ubu.miscompras.model.interactors.ProductGetterByDateInteractor;
import com.ubu.miscompras.model.interactors.ProductGetterByPriceInteractor;
import com.ubu.miscompras.view.fragment.ProductFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Presenter encargado de la vista {@link ProductFragment}
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class ProductFragmentPresenter implements IOnLoadComplete {


    private ProductFragment mView;

    /**
     * Constructor de la clase.
     *
     * @param mView vista asociada.
     */
    public ProductFragmentPresenter(ProductFragment mView) {

        this.mView = mView;

    }

    /**
     * Este método llama al interactor que obtiene las lineas de produtcto por entre dos fechas.
     *
     * @param starDate fecha de incio.
     * @param endDate  fecha de fin.
     */
    public void getProductosByDate(Date starDate, Date endDate) {
        if (endDate.before(starDate)) {
            mView.showErrorDate();
        } else {

            ProductGetterByDateInteractor task = new ProductGetterByDateInteractor(this, starDate, endDate);
            task.execute();
        }
    }

    /**
     * Este método llama al interactor que obtiene las lineas de una category.
     *
     * @param category category.
     */
    public void getProductosByCategoria(Category category) {
        ProductGetterByCategoryInterator task = new ProductGetterByCategoryInterator(this, category);
        task.execute();
    }

    /**
     * Este método llama al interactor que obtiene las lineas de produtcto por entre dos precios.
     *
     * @param minPrice precio minimo.
     * @param maxPrice precio maximo.
     */
    public void getProductosByPrice(String minPrice, String maxPrice) {
        try {
            double min = Double.parseDouble(minPrice);
            double max = Double.parseDouble(maxPrice);

            if (min > max) {
                mView.showPricesError();
            } else {
                ProductGetterByPriceInteractor task = new ProductGetterByPriceInteractor(this, min, max);
                task.execute();
            }
        } catch (NumberFormatException e) {
            mView.showEmptyFieldMessage();
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
    public void loadCompleteCategoria(List<Category> items) {
        if (!items.isEmpty()) {
            mView.setCategorias(items);
        } else {
            mView.showEmptyListMessage();
        }

    }

    @Override
    public void loadCompleteTicketProducto(List<ProductLine> items) {
        if (!items.isEmpty()) {
            mView.setProductLines(items);
            mView.showList();
        } else {
            mView.hideList();
            mView.showEmptyListMessage();
        }

    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {

    }


    @Override
    public void onResume() {
        getCategories();
        Calendar cal = Calendar.getInstance();
        mView.setEndDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        mView.setStartDate(cal.getTime());

    }
}
