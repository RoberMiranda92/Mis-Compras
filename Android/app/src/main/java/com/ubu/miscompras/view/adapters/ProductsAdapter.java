package com.ubu.miscompras.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.Ticket;

import java.util.Calendar;
import java.util.List;

/**
 * Adaptador para las listas de productos.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolderProducts> {

    private Context context;
    private List<ProductLine> itemData;

    private boolean dates = false;
    private boolean prices = false;
    private boolean category = false;


    public ProductsAdapter(Context context) {
        this.context = context;

    }

    @Override
    public ViewHolderProducts onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);


        ViewHolderProducts holder = new ViewHolderProducts(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderProducts holder, int position) {
        ProductLine linea = itemData.get(position);

        int cant = linea.getAmount();
        double precio = linea.getPrice();
        double importe = linea.getTotalImport();
        Product product = linea.getProduct();
        Ticket ticket = linea.getTicket();


        holder.textViewDest.setText(context.getString(R.string.format_cantidad, cant, product.getName()));
        holder.textViewPrice.setVisibility(View.VISIBLE);
        holder.textViewPrice.setText(context.getString(R.string.format_productPrice, precio));
        holder.textViewTotal.setText(context.getString(R.string.format_importe, importe));

        if (dates) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(ticket.getPurchaseDate());
            holder.textViewMain.setText(context.getString(R.string.format_date,
                    cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        }

        if (category) {
            holder.textViewMain.setText(product.getCategory().getName());
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

    /**
     * Este método coloca la lista asociada al adaptador.
     *
     * @param productLines lista de lineas de producto
     */
    public void setProducts(List<ProductLine> productLines) {
        this.itemData = productLines;
    }

    /**
     * Este método activa el campo de fechas, y desactiva el resto.
     */
    public void enableDatesFilter() {
        this.dates = true;
        this.category = false;
        this.prices = false;
    }

    /**
     * Este método activa el campo de precios, y desactiva el resto.
     */
    public void enablePricesFilter() {
        this.dates = false;
        this.category = false;
        this.prices = true;
    }

    /**
     * Este método activa el campo de categorias, y desactiva el resto.
     */
    public void enableCategoryFilter() {
        this.dates = false;
        this.category = true;
        this.prices = false;
    }

    /**
     * Holder de la lista.
     */
    public class ViewHolderProducts extends RecyclerView.ViewHolder {
        public TextView textViewDest;
        public TextView textViewPrice;
        public TextView textViewTotal;
        public TextView textViewMain;
        public ImageView imageViewIcon;

        public ViewHolderProducts(View itemLayoutView) {
            super(itemLayoutView);

            textViewDest = (TextView) itemLayoutView.findViewById(R.id.textView_Descripcion);
            textViewPrice = (TextView) itemLayoutView.findViewById(R.id.textView_Precio);
            textViewTotal = (TextView) itemLayoutView.findViewById(R.id.textView_total);
            textViewMain = (TextView) itemLayoutView.findViewById(R.id.textView_main);
            imageViewIcon = (ImageView) itemLayoutView.findViewById(R.id.imageView_categoryIcon);

        }
    }
}
