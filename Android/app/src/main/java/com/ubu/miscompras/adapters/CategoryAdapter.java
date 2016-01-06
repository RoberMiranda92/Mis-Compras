package com.ubu.miscompras.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.utils.Utils;

import java.util.List;

/**
 * Created by RobertoMiranda on 18/12/15.
 */
public class CategoryAdapter extends BaseAdapter {

    private int viewResource;
    private LayoutInflater inflater;
    private Context context;
    private List<Categoria> itemList;


    public CategoryAdapter(Context context, int viewResourceId, List<Categoria> itemList) {

        this.context = context;
        this.itemList = itemList;
        this.viewResource = viewResourceId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Categoria getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getPosition(Categoria categoria){
        return  itemList.indexOf(categoria);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return  getCustomViewHeader(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {


        View row = inflater.inflate(viewResource, parent, false);

        Categoria category = itemList.get(position);
        ImageView icon = (ImageView) row.findViewById(R.id.imageView_category);
        TextView label = (TextView) row.findViewById(R.id.item_category);

        label.setText(category.getNombre());
        icon.setImageResource(Utils.getCategoryIcon(category.getId()));

        return row;
    }

    public View getCustomViewHeader(int position, View convertView, ViewGroup parent) {


        View row = inflater.inflate(R.layout.header_category, parent, false);

        Categoria category = itemList.get(position);
        ImageView icon = (ImageView) row.findViewById(R.id.imageView_category);
        TextView label = (TextView) row.findViewById(R.id.item_category);

        label.setText(category.getNombre());
        icon.setImageResource(Utils.getCategoryIcon(category.getId()));
        return row;
    }
}
