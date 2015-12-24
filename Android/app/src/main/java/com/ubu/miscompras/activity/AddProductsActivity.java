package com.ubu.miscompras.activity;

import android.content.Context;
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
import com.ubu.miscompras.adapters.ProductosAddAdapter;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.TicketProducto;
import com.ubu.miscompras.presenter.AddProductsPresenter;
import com.ubu.miscompras.utils.VerticalDividerItemDecorator;
import com.ubu.miscompras.views.EditProdutDialog;

import java.util.List;

/**
 * Created by RobertoMiranda on 15/12/15.
 */
public class AddProductsActivity extends AppCompatActivity implements View.OnClickListener, OnItemClick, OnEditableItem {


    private RecyclerView recyclerView_productos;
    private ProductosAddAdapter recyclerView_Adapter;
    private FloatingActionButton saveButton;
    private TextView textView_fecha;
    private TextView textView_total;
    private AddProductsPresenter presenter;
    private boolean disable = false;
    private List<Categoria> categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        String jsonProductos = extras.getString("productos");

        setContentView(R.layout.activity_add_products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView_Adapter = new ProductosAddAdapter(this);
        recyclerView_productos = (RecyclerView) findViewById(R.id.recyclerView_AddProductos);
        recyclerView_productos.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_productos.addItemDecoration(new VerticalDividerItemDecorator(1, false));
        recyclerView_productos.setAdapter(recyclerView_Adapter);


        saveButton = (FloatingActionButton) findViewById(R.id.FloattingButton_saveProducts);
        saveButton.setOnClickListener(this);


        textView_fecha = (TextView) findViewById(R.id.textView_fechaCompra);
        textView_total = (TextView) findViewById(R.id.textView_totalCompra);

        presenter = new AddProductsPresenter(this);
        presenter.setResource(jsonProductos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.newProduct) {
            createNewProdcut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewProdcut() {

        TicketProducto newLinea = new TicketProducto(new Producto(""), 0, 0, 0);
        int position = recyclerView_Adapter.getItemCount();
        FragmentManager fm = getSupportFragmentManager();
        EditProdutDialog editNameDialog = EditProdutDialog.newInstance(categorias, newLinea, position);
        editNameDialog.show(fm, "fragment_edit_name");
    }


    public void setDate(String date) {
        textView_fecha.setText(date);
    }

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
        int itemPosition = recyclerView_productos.getChildPosition(v);
        TicketProducto item = recyclerView_Adapter.getItemAt(itemPosition);
        FragmentManager fm = getSupportFragmentManager();
        EditProdutDialog editNameDialog = EditProdutDialog.newInstance(categorias, item, itemPosition);
        editNameDialog.show(fm, "fragment_edit_name");
    }

    public void setItems(List<TicketProducto> items) {
        recyclerView_Adapter.setProducts(items);
        recyclerView_Adapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.FloattingButton_saveProducts:
                if (!disable)
                    presenter.saveProducts(recyclerView_Adapter.getItems());
                else
                    showMessage("Ya se han guardado los productos");
                break;

        }
    }


    public void showErrorMensage() {
        Toast.makeText(this, "Error al procesar algunos productos", Toast.LENGTH_LONG).show();
    }

    public void disableButton() {
        disable = true;
    }

    public void setCategories(List<Categoria> items) {
        this.categorias = items;
    }

    @Override
    public void OnEditItem(TicketProducto productLine, int position) {
        recyclerView_Adapter.setItemAt(productLine, position);
        recyclerView_Adapter.notifyDataSetChanged();
    }

    @Override
    public void showOnEditItemError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
