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
package com.ubu.miscompras.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.utils.Utils;
import com.ubu.miscompras.view.activity.TicketDetail;

import java.util.List;

/**
 * Adaptador de la lista de productos que se muestra en el la vista de detalle de tiques.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class TicketDetailAdapter extends RecyclerView.Adapter<TicketDetailAdapter.ViewHolderHolderProducts> {

    private TicketDetail mView;
    private List<ProductLine> itemData;


    public TicketDetailAdapter(TicketDetail ticketDetail) {
        this.mView = ticketDetail;
    }

    @Override
    public ViewHolderHolderProducts onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_product, parent, false);

        ViewHolderHolderProducts holder = new ViewHolderHolderProducts(itemLayoutView);

        return holder;

    }


    @Override
    public void onBindViewHolder(ViewHolderHolderProducts holder, int position) {

        ProductLine productLine = itemData.get(position);

        int amount = productLine.getAmount();
        double price = productLine.getPrice();
        double total = productLine.getTotalImport();
        Product product = productLine.getProduct();
        Category categegory = product.getCategory();


        holder.textViewDest.setText(mView.getString(R.string.format_cantidad, amount, product.getName()));
        holder.textViewPrice.setText(mView.getString(R.string.format_productPrice, price));
        holder.textViewTotal.setText(mView.getString(R.string.format_importe, total));
        if (categegory != null)
            holder.imageViewIcon.setImageResource(Utils.getCategoryIcon(categegory.getId()));
        else
            holder.imageViewIcon.setImageResource(Utils.getCategoryIcon(Category.OTROS));

    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    /**
     * Este método coloca una lista de lineas de producto en el adapter.
     *
     * @param productLines lista de lineas de producto
     */
    public void setProducts(List<ProductLine> productLines) {
        this.itemData = productLines;
    }


    /**
     * Este método devuelve la lista de elementos.
     *
     * @return lista de lineas de productos.
     */
    public List<ProductLine> getItems() {
        return itemData;
    }

    /**
     * Este método devuelve una linea de producto en la posición dada.
     *
     * @param itemPosition posición en la lista.
     * @return linea de producto en la posición.
     */
    public ProductLine getItemAt(int itemPosition) {
        return itemData.get(itemPosition);
    }

    /**
     * Este método añade un elemento a la lista en la posición dada.
     *
     * @param productLine linea de producto.
     * @param position    posición
     */
    public void setItemAt(ProductLine productLine, int position) {
        if (position == getItemCount())
            itemData.add(position, productLine);
        else
            itemData.set(position, productLine);

    }

    /**
     * Holder de la lista.
     */
    public static class ViewHolderHolderProducts extends RecyclerView.ViewHolder {
        public TextView textViewDest;
        public TextView textViewPrice;
        public TextView textViewTotal;
        public ImageView imageViewIcon;

        public ViewHolderHolderProducts(View itemLayoutView) {
            super(itemLayoutView);

            textViewDest = (TextView) itemLayoutView.findViewById(R.id.textView_Descripcion);
            textViewPrice = (TextView) itemLayoutView.findViewById(R.id.textView_Precio);
            textViewTotal = (TextView) itemLayoutView.findViewById(R.id.textView_total);
            imageViewIcon = (ImageView) itemLayoutView.findViewById(R.id.imageView_categoryIcon);

        }
    }
}
