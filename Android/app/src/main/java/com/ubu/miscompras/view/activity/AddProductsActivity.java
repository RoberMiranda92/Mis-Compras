/*
*   Copyright (C) 2015 Roberto Miranda.
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.ubu.miscompras.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.presenter.AddProductsPresenter;
import com.ubu.miscompras.utils.VerticalDividerItemDecorator;
import com.ubu.miscompras.view.adapters.ProductAddAdapter;
import com.ubu.miscompras.view.customViews.EditProductDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Activity que se encarga de mostrar al usuario la ventana que permite añadir
 * las nuevas lineas de producto.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class AddProductsActivity extends AppCompatActivity implements View.OnClickListener, IOnItemClick, IOnEditableItem {


    private RecyclerView recyclerView_productLine;
    private ProductAddAdapter recyclerView_Adapter;
    private FloatingActionButton saveButton;
    private TextView textView_date;
    private TextView textView_total;
    private AddProductsPresenter presenter;
    private List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        String jsonProductos = extras.getString("productos");

        setContentView(R.layout.activity_add_products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView_Adapter = new ProductAddAdapter(this);
        recyclerView_productLine = (RecyclerView) findViewById(R.id.recyclerView_AddProductos);
        recyclerView_productLine.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_productLine.addItemDecoration(new VerticalDividerItemDecorator(1, false));
        recyclerView_productLine.setAdapter(recyclerView_Adapter);


        saveButton = (FloatingActionButton) findViewById(R.id.FloattingButton_saveProducts);
        saveButton.setOnClickListener(this);


        textView_date = (TextView) findViewById(R.id.textView_fechaCompra);
        textView_total = (TextView) findViewById(R.id.textView_totalCompra);

        presenter = new AddProductsPresenter(this);
        setItems(getProdcutFromJson(jsonProductos));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_add_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.newProduct) {
            createNewProductLine();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Este método crea una nueva linea de producto vacia.
     */
    private void createNewProductLine() {
        ProductLine newProductLine = new ProductLine(new Product(""), 0, 0, 0);
        int position = recyclerView_Adapter.getItemCount();
        FragmentManager fm = getSupportFragmentManager();
        EditProductDialog editProductDialog = EditProductDialog.newInstance(categoryList, newProductLine, position);
        editProductDialog.show(fm, "fragment_edit_dialog");
    }

    /**
     * Este método coloca la fecha en el TexView de Fecha.
     *
     * @param date fecha a colocar
     */
    public void setDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        textView_date.setText(getString(R.string.format_date, day, month + 1, year));
    }

    /**
     * Este método coloca el total en el textView de Importe Total
     *
     * @param total a colocar
     */
    public void setTotal(double total) {
        textView_total.setText(getString(R.string.format_total, total));
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onItemClick(View v) {
        int itemPosition = recyclerView_productLine.getChildPosition(v);
        ProductLine item = recyclerView_Adapter.getItemAt(itemPosition);
        FragmentManager fm = getSupportFragmentManager();
        EditProductDialog editNameDialog = EditProductDialog.newInstance(categoryList, item, itemPosition);
        editNameDialog.show(fm, "fragment_edit_name");
    }

    /**
     * Este método coloca las lineas de producto en la lista.
     *
     * @param productLinesList lineas de producto de la lista.
     */
    public void setItems(List<ProductLine> productLinesList) {
        recyclerView_Adapter.setProducts(productLinesList);
        recyclerView_Adapter.notifyDataSetChanged();
        setTotal(calculateTotal());

    }

    /**
     * Muestra el mensaje de edición.
     */
    public void showEditMessage() {
        Toast.makeText(this, getString(R.string.category_message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.FloattingButton_saveProducts:
                presenter.saveProducts(recyclerView_Adapter.getItems());
                presenter.uploadProducts(recyclerView_Adapter.getItems());
                break;

        }
    }

    /**
     * Este mñetodo muestra un mesaje de error al procesar incorrectamente los productos.
     */
    public void showErrorMensage() {
        Toast.makeText(this, getString(R.string.errorProcessProducts), Toast.LENGTH_SHORT).show();
    }

    /**
     * Este metodo finaliza la actividad.
     */
    public void end() {
        super.finish();
    }

    /**
     * Este método coloca la lista de categorias.
     *
     * @param categoryList lista de categorias.
     */
    public void setCategories(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public void OnEditItem(ProductLine productLine, int position) {
        recyclerView_Adapter.setItemAt(productLine, position);
        recyclerView_Adapter.notifyDataSetChanged();
        setTotal(calculateTotal());
    }

    /**
     * Este método recalcula el total acumulado del importe de las lineas de producto.
     */
    private double calculateTotal() {

        List<ProductLine> lines = recyclerView_Adapter.getItems();
        double total = 0;

        for (ProductLine l : lines) {
            total += l.getTotalImport();
        }
        return total;
    }

    @Override
    public void showOnEditItemError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Muestra el mensaje de lista vacia.
     */
    public void showEmptyMessage() {
        Toast.makeText(this, getString(R.string.emptyList), Toast.LENGTH_SHORT).show();
    }

    public void showOkMessage() {
        Toast.makeText(this, getString(R.string.saveProducts), Toast.LENGTH_SHORT).show();
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
                    showErrorMensage();
                }

            }
        } catch (Exception e) {
            showErrorMensage();
        }
        return lineasProducto;
    }

    /**
     * Este método actualiza el total gastado en compras.
     *
     */
    public void updateTotal() {

        float total = (float) calculateTotal();

        SharedPreferences sharedPref = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("importeTotal", total);
        editor.apply();

    }
}
