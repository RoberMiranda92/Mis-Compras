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

import android.content.Context;
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
import com.ubu.miscompras.view.activity.IOnItemClick;

import java.util.List;

/**
 * Adaptador para la lista de productos que se muestran a la hora de
 * guardar nuevos en la base de datos.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class ProductAddAdapter extends RecyclerView.Adapter<ProductAddAdapter.ViewHolderProducts> {

    private Context contex;
    private IOnItemClick mView;
    private List<ProductLine> itemData;

    /**
     * Constructor de la clase.
     *
     * @param mView vista.
     */
    public ProductAddAdapter(IOnItemClick mView) {
        this.mView = mView;
        this.contex = mView.getContext();

    }

    @Override
    public ViewHolderProducts onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_product, parent, false);

        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.onItemClick(v);
            }
        });

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
        Category category = product.getCategory();


        if (precio * cant != importe)
            holder.textViewTotal.setTextColor(contex.getResources().getColor(R.color.red));
        else
            holder.textViewTotal.setTextColor(contex.getResources().getColor(R.color.green));

        holder.textViewDest.setText(contex.getString(R.string.format_cantidad, cant, product.getName()));
        holder.textViewPrice.setText(contex.getString(R.string.format_productPrice, precio));
        holder.textViewTotal.setText(contex.getString(R.string.format_importe, importe));
        if (category != null)
            holder.imageViewIcon.setImageResource(Utils.getCategoryIcon(category.getId()));
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
     * Holder que hace referencia a los elementos que contiene la fila.
     */
    public static class ViewHolderProducts extends RecyclerView.ViewHolder {
        public TextView textViewDest;
        public TextView textViewPrice;
        public TextView textViewTotal;
        public ImageView imageViewIcon;

        public ViewHolderProducts(View itemLayoutView) {
            super(itemLayoutView);

            textViewDest = (TextView) itemLayoutView.findViewById(R.id.textView_Descripcion);
            textViewPrice = (TextView) itemLayoutView.findViewById(R.id.textView_Precio);
            textViewTotal = (TextView) itemLayoutView.findViewById(R.id.textView_total);
            imageViewIcon = (ImageView) itemLayoutView.findViewById(R.id.imageView_categoryIcon);

        }
    }
}
