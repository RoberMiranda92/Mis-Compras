package com.ubu.miscompras.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.activity.OnItemClick;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.TicketProducto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RobertoMiranda on 18/11/15.
 */
public class ProductosAddAdapter extends RecyclerView.Adapter<ProductosAddAdapter.ViewHolderProductos> {

    private Context contex;
    private OnItemClick mView;
    private List<TicketProducto> itemData;

    public ProductosAddAdapter(OnItemClick mView, ArrayList<TicketProducto> itemData) {
        this.mView = mView;
        this.itemData = itemData;
    }

    public ProductosAddAdapter(OnItemClick mView) {
        this.mView = mView;
        this.contex = mView.getContext();

    }

    @Override
    public ProductosAddAdapter.ViewHolderProductos onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_product, parent, false);

        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.onItemClick(v);
            }
        });

        ViewHolderProductos holder = new ViewHolderProductos(itemLayoutView);

        return holder;

    }


    @Override
    public void onBindViewHolder(ProductosAddAdapter.ViewHolderProductos holder, int position) {

        TicketProducto linea = itemData.get(position);

        int cant = linea.getCantidad();
        double precio = linea.getPrecio();
        double importe = linea.getImporte();
        Producto producto = linea.getProducto();


        if (precio * cant != importe)
            holder.textViewTotal.setTextColor(contex.getResources().getColor(R.color.red));
        else
            holder.textViewTotal.setTextColor(contex.getResources().getColor(R.color.green));

        holder.textViewDest.setText(contex.getString(R.string.format_cantidad, cant, producto.getNombre()));
        holder.textViewPrice.setText(contex.getString(R.string.format_productPrice, precio));
        holder.textViewTotal.setText(contex.getString(R.string.format_importe, importe));


    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public void setProducts(List<TicketProducto> products) {
        this.itemData = products;
    }

    public List<TicketProducto> getItems() {
        return itemData;
    }

    public TicketProducto getItemAt(int itemPosition) {
        return itemData.get(itemPosition);
    }

    public void setItemAt(TicketProducto productLine, int position) {
        if (position == getItemCount())
            itemData.add(position, productLine);
        else
            itemData.set(position, productLine);

    }


    public static class ViewHolderProductos extends RecyclerView.ViewHolder {
        public TextView textViewDest;
        public TextView textViewPrice;
        public TextView textViewTotal;

        public ViewHolderProductos(View itemLayoutView) {
            super(itemLayoutView);

            textViewDest = (TextView) itemLayoutView.findViewById(R.id.textView_Descripcion);
            textViewPrice = (TextView) itemLayoutView.findViewById(R.id.textView_Precio);
            textViewTotal = (TextView) itemLayoutView.findViewById(R.id.textView_total);

        }
    }
}
