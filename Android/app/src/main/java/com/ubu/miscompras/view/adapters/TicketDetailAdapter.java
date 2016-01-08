package com.ubu.miscompras.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.view.activity.TicketDetail;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.utils.Utils;

import java.util.List;

/**
 * Created by RobertoMiranda on 5/1/16.
 */
public class TicketDetailAdapter extends RecyclerView.Adapter<TicketDetailAdapter.ViewHolderProductos> {

    private TicketDetail mView;
    private List<LineaProducto> itemData;


    public TicketDetailAdapter(TicketDetail ticketDetail) {
        this.mView = ticketDetail;
    }

    @Override
    public TicketDetailAdapter.ViewHolderProductos onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_product, parent, false);

        ViewHolderProductos holder = new ViewHolderProductos(itemLayoutView);

        return holder;

    }


    @Override
    public void onBindViewHolder(TicketDetailAdapter.ViewHolderProductos holder, int position) {

        LineaProducto linea = itemData.get(position);

        int cant = linea.getCantidad();
        double precio = linea.getPrecio();
        double importe = linea.getImporte();
        Producto producto = linea.getProducto();
        Categoria categoria = producto.getCategoria();


        holder.textViewDest.setText(mView.getString(R.string.format_cantidad, cant, producto.getNombre()));
        holder.textViewPrice.setText(mView.getString(R.string.format_productPrice, precio));
        holder.textViewTotal.setText(mView.getString(R.string.format_importe, importe));
        if (categoria != null)
            holder.imageViewIcon.setImageResource(Utils.getCategoryIcon(categoria.getId()));
        else
            holder.imageViewIcon.setImageResource(Utils.getCategoryIcon(Categoria.OTROS));

    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public void setProducts(List<LineaProducto> products) {
        this.itemData = products;
    }

    public List<LineaProducto> getItems() {
        return itemData;
    }

    public LineaProducto getItemAt(int itemPosition) {
        return itemData.get(itemPosition);
    }

    public void setItemAt(LineaProducto productLine, int position) {
        if (position == getItemCount())
            itemData.add(position, productLine);
        else
            itemData.set(position, productLine);

    }


    public static class ViewHolderProductos extends RecyclerView.ViewHolder {
        public TextView textViewDest;
        public TextView textViewPrice;
        public TextView textViewTotal;
        public ImageView imageViewIcon;

        public ViewHolderProductos(View itemLayoutView) {
            super(itemLayoutView);

            textViewDest = (TextView) itemLayoutView.findViewById(R.id.textView_Descripcion);
            textViewPrice = (TextView) itemLayoutView.findViewById(R.id.textView_Precio);
            textViewTotal = (TextView) itemLayoutView.findViewById(R.id.textView_total);
            imageViewIcon = (ImageView) itemLayoutView.findViewById(R.id.imageView_categoryIcon);

        }
    }
}
