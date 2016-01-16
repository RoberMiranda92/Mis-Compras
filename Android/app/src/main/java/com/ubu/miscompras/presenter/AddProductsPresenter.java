package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.CategoryGetterInteractor;
import com.ubu.miscompras.model.interactors.ProductInsertIterator;
import com.ubu.miscompras.model.interactors.UploadProductLineInteractor;
import com.ubu.miscompras.view.activity.AddProductsActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Presenter encargado de la vista de añadir produtos {@link AddProductsActivity}
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class AddProductsPresenter implements IOnFinishedListener, IOnLoadComplete {


    private AddProductsActivity mainView;
    private String json = "";

    public AddProductsPresenter(AddProductsActivity mainView) {
        this.mainView = mainView;
    }


    @Override
    public void onResume() {

        Calendar cal = Calendar.getInstance();
        mainView.setDate(cal.getTime());
        getCategories();

        mainView.showEditMessage();



    }

    /**
     * Este método comunica a su vista que muestre un mensaje de error;
     *
     * @param message mensaje a mostrar.
     */
    public void showError(String message) {
        mainView.showMessage(message);

    }

    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteLine(List<ProductLine> items) {

    }

    @Override
    public void loadCompleteTicket(List<Ticket> items) {

    }

    @Override
    public void getCategories() {
        CategoryGetterInteractor task = new CategoryGetterInteractor(this);
        task.execute();
    }

    @Override
    public void loadCompleteCategory(List items) {
        mainView.setCategories(items);
    }



    /**
     * Este método llama al interactor para guardar los productos en la BD.
     *
     * @param lineasDeProducto lineas de producto a guardar.
     */
    public void saveProducts(List<ProductLine> lineasDeProducto) {
        if (!lineasDeProducto.isEmpty()) {
            ProductInsertIterator iterator = new ProductInsertIterator(this, lineasDeProducto);
            iterator.execute();
        } else {
            mainView.showEmptyMessage();
        }

    }


    @Override
    public void onFinished(Boolean result) {
        if (result) {
            mainView.showOkMessage();
            mainView.end();
        } else
            mainView.showMessage("Error al guardar los productos");
    }

    /**
     * Este metodo llama a un interactor para guardar las lineas de producto en el servidor.
     *
     * @param items lineas de producto que se quieren subir.
     */
    public void uploadProducts(List<ProductLine> items) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            if (i != items.size() - 1) {
                builder.append(items.get(i).getProduct().getName());
                builder.append(",");
            } else {
                builder.append(items.get(i).getProduct().getName());
            }
        }

        UploadProductLineInteractor task = new UploadProductLineInteractor(this, builder.toString());
        task.execute();

    }
}
