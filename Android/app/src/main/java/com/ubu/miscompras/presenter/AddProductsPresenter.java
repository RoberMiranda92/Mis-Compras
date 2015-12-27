package com.ubu.miscompras.presenter;

import android.content.Context;

import com.ubu.miscompras.activity.AddProductsActivity;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.TicketProducto;
import com.ubu.miscompras.task.CategoryGetterInteractor;
import com.ubu.miscompras.task.ProductInsertIterator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by RobertoMiranda on 16/12/15.
 */
public class AddProductsPresenter implements MainPresenter, OnFinishedListener, OnLoadComplete {


    private double total;
    private AddProductsActivity mainView;
    private String json;

    public AddProductsPresenter(AddProductsActivity mainView) {

        this.mainView = mainView;
    }


    @Override
    public void onResume() {

        if (!json.isEmpty()) {
            List<TicketProducto> lineas = getProdcutFromJson(json);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            mainView.setDate(dateFormat.format(cal.getTime()));
            mainView.setTotal(total);
            mainView.setItems(lineas);
            CategoryGetterInteractor task = new CategoryGetterInteractor(this);
            task.execute();

        } else {
            mainView.showErrorMensage();
        }


    }

    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteTicketProducto(List<TicketProducto> items) {

    }

    @Override
    public void loadCompleteCategoria(List items) {
        mainView.setCategories(items);
    }

    @Override
    public Context getContext() {
        return mainView;
    }

    @Override
    public void onItemClicked(int position) {

    }

    public void setResource(String json) {
        this.json = json;

    }

    public void saveProducts(List<TicketProducto> lineasDeProducto) {
        ProductInsertIterator iterator = new ProductInsertIterator(this);
        iterator.execute(lineasDeProducto);

    }


    private ArrayList<TicketProducto> getProdcutFromJson(String ArrayJson) {
        ArrayList<TicketProducto> lineasProducto = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(ArrayJson);

            for (int i = 0; i < array.length(); i++) {

                JSONObject arrayLineasProducto = array.getJSONObject(i);

                String cantidad = arrayLineasProducto.getString("cantidad");
                String nombre = arrayLineasProducto.getString("nombre");
                String precio = arrayLineasProducto.getString("precio");
                String importe = arrayLineasProducto.getString("total");
                try {
                    Producto producto = new Producto(nombre);
                    TicketProducto lineaDeProducto = new TicketProducto(producto,
                            Integer.parseInt(cantidad), Double.parseDouble(precio),
                            Double.parseDouble(importe));
                    total += Double.parseDouble(importe);
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
            mainView.disableButton();
            mainView.showMessage("Productos guardados correctamente");
        } else
            mainView.showMessage("Error al guardar los productos");
    }
}
