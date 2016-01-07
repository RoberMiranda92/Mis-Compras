package com.ubu.miscompras.presenter;

import com.ubu.miscompras.activity.AddProductsActivity;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.task.CategoryGetterInteractor;
import com.ubu.miscompras.task.ProductInsertIterator;
import com.ubu.miscompras.task.UploadProductLineInteractor;

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
public class AddProductsPresenter implements OnFinishedListener, OnLoadComplete {


    private AddProductsActivity mainView;
    private String json = "";

    public AddProductsPresenter(AddProductsActivity mainView) {
        this.mainView = mainView;
    }


    @Override
    public void onResume() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        mainView.setDate(dateFormat.format(cal.getTime()));
        getCategories();

        if (!json.isEmpty()) {
            List<LineaProducto> lineas = getProdcutFromJson(json);
            mainView.setItems(lineas);
            mainView.showEditMessage();

        }


    }

    public void showError(String message) {
        mainView.showMessage(message);

    }

    @Override
    public void showError() {

    }

    @Override
    public void loadCompleteTicketProducto(List<LineaProducto> items) {

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

    public void saveProducts(List<LineaProducto> lineasDeProducto) {
        if(!lineasDeProducto.isEmpty()){
        ProductInsertIterator iterator = new ProductInsertIterator(this);
        iterator.execute(lineasDeProducto);}
        else{
            mainView.showEmptyMessage();
        }

    }


    private ArrayList<LineaProducto> getProdcutFromJson(String ArrayJson) {
        ArrayList<LineaProducto> lineasProducto = new ArrayList<>();
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
                    LineaProducto lineaDeProducto = new LineaProducto(producto,
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
            mainView.showMessage("Productos guardados correctamente");
            mainView.end();
        } else
            mainView.showMessage("Error al guardar los productos");
    }

    public void uploadProducts(List<LineaProducto> items) {

        StringBuilder builder = new StringBuilder();

        for(int i=0;i<items.size();i++){
            if(i!=items.size()-1){
                builder.append(items.get(i).getProducto().getNombre());
                builder.append(",");
            }else{
                builder.append(items.get(i).getProducto().getNombre());
            }
        }

        UploadProductLineInteractor task = new UploadProductLineInteractor(this,builder.toString());
        task.execute();

    }
}
