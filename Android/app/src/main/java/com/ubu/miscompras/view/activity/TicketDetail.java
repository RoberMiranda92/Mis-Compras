package com.ubu.miscompras.view.activity;

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
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.TicketDetailPresenter;
import com.ubu.miscompras.utils.VerticalDividerItemDecorator;
import com.ubu.miscompras.view.adapters.TicketDetailAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Vista de detalle de los tiquets.
 */
public class TicketDetail extends AppCompatActivity {


    private RecyclerView recyclerView_productLineList;
    private TicketDetailAdapter recyclerView_Adapter;
    private FloatingActionButton saveButton;
    private TextView textView_date;
    private TextView textView_total;
    private TicketDetailPresenter presenter;
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
        recyclerView_productLineList = (RecyclerView) findViewById(R.id.recyclerView_AddProductos);
        recyclerView_productLineList.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_productLineList.addItemDecoration(new VerticalDividerItemDecorator(1, false));


        saveButton = (FloatingActionButton) findViewById(R.id.FloattingButton_saveProducts);
        saveButton.setVisibility(View.GONE);


        textView_date = (TextView) findViewById(R.id.textView_fechaCompra);
        textView_total = (TextView) findViewById(R.id.textView_totalCompra);

        presenter = new TicketDetailPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.getTicketLinesByTicket(ticket);

        setDate(ticket.getPurchaseDate());
        setTotal(ticket.getTotal());

    }

    /**
     * Este método coloca la fecha en el TexView de Fecha.
     *
     * @param date fecha a colocar.
     */
    public void setDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        textView_date.setText(this.getString(R.string.format_date,
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
    }

    /**
     * Este método coloca el total en el textView de Importe Total
     *
     * @param total a colocar.
     */
    public void setTotal(double total) {
        textView_total.setText(getString(R.string.format_total, total));
    }


    /**
     * Este método coloca las lineas de producto en la lista.
     *
     * @param productLinesList lineas de producto de la lista.
     */
    public void setItems(List<ProductLine> productLinesList) {
        recyclerView_Adapter.setProducts(productLinesList);
        recyclerView_productLineList.setAdapter(recyclerView_Adapter);
        recyclerView_Adapter.notifyDataSetChanged();
        setTotal(calculateTotal());

    }


    public void showErrorMensage() {
        Toast.makeText(this, "Error al procesar algunos productos", Toast.LENGTH_LONG).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }

}
