package com.ubu.miscompras.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.adapters.TicketDetailAdapter;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.TicketDetaiPresenter;
import com.ubu.miscompras.utils.VerticalDividerItemDecorator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by RobertoMiranda on 29/12/15.
 */
public class TicketDetail extends AppCompatActivity {


    private RecyclerView recyclerView_productos;
    private TicketDetailAdapter recyclerView_Adapter;
    private FloatingActionButton saveButton;
    private TextView textView_fecha;
    private TextView textView_total;
    private TicketDetaiPresenter presenter;
    private boolean disable = false;
    private List<Categoria> categorias;
    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        ticket = extras.getParcelable("ticket");

        setContentView(R.layout.activity_add_products);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView_Adapter = new TicketDetailAdapter(this);
        recyclerView_productos = (RecyclerView) findViewById(R.id.recyclerView_AddProductos);
        recyclerView_productos.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_productos.addItemDecoration(new VerticalDividerItemDecorator(1, false));
        //recyclerView_productos.setAdapter(recyclerView_Adapter);


        saveButton = (FloatingActionButton) findViewById(R.id.FloattingButton_saveProducts);
        saveButton.setVisibility(View.GONE);


        textView_fecha = (TextView) findViewById(R.id.textView_fechaCompra);
        textView_total = (TextView) findViewById(R.id.textView_totalCompra);

        presenter = new TicketDetaiPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();

        presenter.getTicketLinesByTicket(ticket);

        setDate(ticket.getFecha_compra());
        setTotal(ticket.getTotal());

    }


    public void setDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);

        textView_fecha.setText(this.getString(R.string.format_date,
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
    }

    public void setTotal(double total) {
        textView_total.setText(getString(R.string.format_total, total));
    }


    public void setItems(List<LineaProducto> items) {
        recyclerView_productos.setAdapter(recyclerView_Adapter);
        recyclerView_Adapter.setProducts(items);
        recyclerView_Adapter.notifyDataSetChanged();
        calculateTotal();

    }


    public void showErrorMensage() {
        Toast.makeText(this, "Error al procesar algunos productos", Toast.LENGTH_LONG).show();
    }


    private void calculateTotal() {

        List<LineaProducto> lines = recyclerView_Adapter.getItems();
        double total = 0;

        for (LineaProducto l : lines) {
            total += l.getImporte();
        }
        setTotal(total);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

}
