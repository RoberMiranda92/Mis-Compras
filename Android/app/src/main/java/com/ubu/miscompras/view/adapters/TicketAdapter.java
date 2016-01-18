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
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.view.activity.IOnItemClick;

import java.util.Calendar;
import java.util.List;

/**
 * Adaptador de la lista de tiques.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolderTicket> {

    private IOnItemClick mView;
    private Context context;
    private List<Ticket> itemData;

    private boolean dates = false;
    private boolean prices = false;


    public TicketAdapter(IOnItemClick activity) {
        this.mView = activity;
        this.context = activity.getContext();

    }

    @Override
    public ViewHolderTicket onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);


        itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.onItemClick(v);
            }
        });

        ViewHolderTicket holder = new ViewHolderTicket(itemLayoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderTicket holder, int position) {
        Ticket ticket = itemData.get(position);

        int num = ticket.getId();
        int amount = ticket.getProductAmount();
        double total = ticket.getTotal();


        holder.textViewDest.setText(context.getString(R.string.format_comra, num));
        holder.textViewArticulos.setText(context.getString(R.string.format_articulos, amount));

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(ticket.getPurchaseDate());

        if (dates) {
            holder.textViewMain.setText(context.getString(R.string.format_date,
                    cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
            holder.textViewTotal.setText(context.getString(R.string.format_importe, total));
        }

        if (prices) {
            holder.textViewTotal.setText(context.getString(R.string.format_date2,
                    cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
            holder.textViewMain.setText(context.getString(R.string.format_importeTotal, total));
        }

    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public void setProducts(List<Ticket> products) {
        this.itemData = products;
    }

    /**
     * Este método activa el campo de fechas, y desactiva el resto.
     */
    public void enableDatesFilter() {
        this.dates = true;
        this.prices = false;
    }

    /**
     * Este método activa el campo de precios, y desactiva el resto.
     */
    public void enablePricesFilter() {
        this.dates = false;
        this.prices = true;
    }

    /**
     * Este método devuelve el ticket de la posición dada.
     *
     * @param itemPosition posición en la lista.
     * @return ticket en la posición dada.
     */
    public Ticket getItemAt(int itemPosition) {
        return itemData.get(itemPosition);
    }

    /**
     * Holder de la lista.
     */
    public class ViewHolderTicket extends RecyclerView.ViewHolder {

        public TextView textViewArticulos;
        public TextView textViewDest;
        public TextView textViewTotal;
        public TextView textViewMain;

        public ViewHolderTicket(View itemLayoutView) {
            super(itemLayoutView);

            textViewDest = (TextView) itemLayoutView.findViewById(R.id.textView_Descripcion);
            textViewTotal = (TextView) itemLayoutView.findViewById(R.id.textView_total);
            textViewMain = (TextView) itemLayoutView.findViewById(R.id.textView_main);
            textViewArticulos = (TextView) itemLayoutView.findViewById(R.id.textView_narticulos);

        }
    }
}
