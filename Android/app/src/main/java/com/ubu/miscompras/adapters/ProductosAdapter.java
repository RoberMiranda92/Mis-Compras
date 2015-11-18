package com.ubu.miscompras.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ubu.miscompras.model.Producto;

/**
 * Created by RobertoMiranda on 18/11/15.
 */
public class ProductosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    private class MyProductHolder extends RecyclerView.ViewHolder {

        public MyProductHolder(View itemView) {
            super(itemView);
        }
    }
}
