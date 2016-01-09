package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.interactors.CategoryGetterInteractor;
import com.ubu.miscompras.model.interactors.ProductInsertIterator;
import com.ubu.miscompras.model.interactors.UploadProductLineInteractor;
import com.ubu.miscompras.view.activity.AddProductsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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

        if (!json.isEmpty()) {
            List<ProductLine> lineas = getProdcutFromJson(json);
            mainView.setItems(lineas);
            mainView.showEditMessage();

        }


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
    public void loadCompleteTicketProducto(List<ProductLine> items) {

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
    public void loadCompleteCategoria(List items) {
        mainView.setCategories(items);
    }


    public void setResource(String json) {
        this.json = json;

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

    /**
     * Este método trasforma un Json en linea de producto.
     *
     * @param ArrayJson json.
     * @return lista de lineas de producto.
     */
    private ArrayList<ProductLine> getProdcutFromJson(String ArrayJson) {
        ArrayList<ProductLine> lineasProducto = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(ArrayJson);

            for (int i = 0; i < array.length(); i++) {

                JSONObject arrayLineasProducto = array.getJSONObject(i);

                String cantidad = arrayLineasProducto.getString("cantidad");
                String nombre = arrayLineasProducto.getString("nombre");
                String precio = arrayLineasProducto.getString("precio");
                String importe = arrayLineasProducto.getString("total");
                try {
                    Product product = new Product(nombre);
                    ProductLine lineaDeProducto = new ProductLine(product,
                            Integer.parseInt(cantidad), Double.parseDouble(precio),
                            Double.parseDouble(importe));
                    lineasProducto.add(lineaDeProducto);
                } catch (NumberFormatException ex) {
                    mainView.showErrorMensage();
                }

            }
        } catch (Exception e) {
            mainView.showErrorMensage();
        }
        return lineasProducto;
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
     * Este mñetodo llama a un interactor para guardar las lineas de producto en el servidor.
     *
     * @param items
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
