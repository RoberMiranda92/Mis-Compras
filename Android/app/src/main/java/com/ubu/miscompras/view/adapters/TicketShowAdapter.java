package com.ubu.miscompras.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.view.activity.OnItemClick;
import com.ubu.miscompras.model.Ticket;

import java.util.Calendar;
import java.util.List;

/**
 * Created by RobertoMiranda on 29/12/15.
 */
public class TicketShowAdapter extends RecyclerView.Adapter<TicketShowAdapter.ViewHolderProductos> {

    private OnItemClick mView;
    private Context context;
    private List<Ticket> itemData;

    private boolean dates = false;
    private boolean prices = false;


    public TicketShowAdapter(OnItemClick activity) {
        this.mView = activity;
        this.context = activity.getContext();

    }

    @Override
    public ViewHolderProductos onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);


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
    public void onBindViewHolder(ViewHolderProductos holder, int position) {
        Ticket ticket = itemData.get(position);

        int num = ticket.getId();
        int can = ticket.getnArticulos();
        double total = ticket.getTotal();


        holder.textViewDest.setText(context.getString(R.string.format_comra, num));
        holder.textViewArticulos.setText(context.getString(R.string.format_articulos, can));

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(ticket.getFecha_compra());

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


    public void enableDatesFilter() {
        this.dates = true;
        this.prices = false;
    }

    public void enablePricesFilter() {
        this.dates = false;
        this.prices = true;
    }

    public Ticket getItemAt(int itemPosition) {
        return itemData.get(itemPosition);
    }


    public class ViewHolderProductos extends RecyclerView.ViewHolder {

        public TextView textViewArticulos;
        public TextView textViewDest;
        public TextView textViewTotal;
        public TextView textViewMain;

        public ViewHolderProductos(View itemLayoutView) {
            super(itemLayoutView);

            textViewDest = (TextView) itemLayoutView.findViewById(R.id.textView_Descripcion);
            textViewTotal = (TextView) itemLayoutView.findViewById(R.id.textView_total);
            textViewMain = (TextView) itemLayoutView.findViewById(R.id.textView_main);
            textViewArticulos = (TextView) itemLayoutView.findViewById(R.id.textView_narticulos);

        }
    }
}
