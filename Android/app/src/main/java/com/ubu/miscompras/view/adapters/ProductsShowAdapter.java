package com.ubu.miscompras.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;

import java.util.Calendar;
import java.util.List;

/**
 * Created by RobertoMiranda on 19/12/15.
 */
public class ProductsShowAdapter extends RecyclerView.Adapter<ProductsShowAdapter.ViewHolderProductos> {

    private Context context;
    private List<LineaProducto> itemData;

    private boolean dates = false;
    private boolean prices = false;
    private boolean category = false;


    public ProductsShowAdapter(Context context) {
        this.context = context;

    }

    @Override
    public ViewHolderProductos onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);


        ViewHolderProductos holder = new ViewHolderProductos(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderProductos holder, int position) {
        LineaProducto linea = itemData.get(position);

        int cant = linea.getCantidad();
        double precio = linea.getPrecio();
        double importe = linea.getImporte();
        Producto producto = linea.getProducto();
        Ticket ticket = linea.getTicket();


        holder.textViewDest.setText(context.getString(R.string.format_cantidad, cant, producto.getNombre()));
        holder.textViewPrice.setVisibility(View.VISIBLE);
        holder.textViewPrice.setText(context.getString(R.string.format_productPrice, precio));
        holder.textViewTotal.setText(context.getString(R.string.format_importe, importe));

        if (dates) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(ticket.getFecha_compra());

            holder.textViewMain.setText(context.getString(R.string.format_date,
                    cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        }

        if (category) {
            holder.textViewMain.setText(producto.getCategoria().getNombre());
        }

        if (prices) {
            holder.textViewPrice.setVisibility(View.GONE);
            holder.textViewMain.setText(context.getString(R.string.format_productPrice, precio));
        }

    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public void setProducts(List<LineaProducto> products) {
        this.itemData = products;
    }


    public void enableDatesFilter() {
        this.dates = true;
        this.category = false;
        this.prices = false;
    }

    public void enablePricesFilter() {
        this.dates = false;
        this.category = false;
        this.prices = true;
    }

    public void enableCategoryFilter() {
        this.dates = false;
        this.category = true;
        this.prices = false;
    }

    public class ViewHolderProductos extends RecyclerView.ViewHolder {
        public TextView textViewDest;
        public TextView textViewPrice;
        public TextView textViewTotal;
        public TextView textViewMain;
        public ImageView imageViewIcon;

        public ViewHolderProductos(View itemLayoutView) {
            super(itemLayoutView);

            textViewDest = (TextView) itemLayoutView.findViewById(R.id.textView_Descripcion);
            textViewPrice = (TextView) itemLayoutView.findViewById(R.id.textView_Precio);
            textViewTotal = (TextView) itemLayoutView.findViewById(R.id.textView_total);
            textViewMain = (TextView) itemLayoutView.findViewById(R.id.textView_main);
            imageViewIcon = (ImageView) itemLayoutView.findViewById(R.id.imageView_categoryIcon);

        }
    }
}
