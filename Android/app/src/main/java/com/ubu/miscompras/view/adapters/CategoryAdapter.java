package com.ubu.miscompras.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.utils.Utils;

import java.util.List;

/**
 * Adaptador para los Spinner que muestran las categorias.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class CategoryAdapter extends BaseAdapter {

    private int viewResource;
    private LayoutInflater inflater;
    private List<Category> itemList;

    /**
     * Constructor de la calse.
     *
     * @param context contexto de la aplicación.
     * @param viewResourceId id del Layout a inflar.
     * @param categoryList  lista de categorias.
     */
    public CategoryAdapter(Context context, int viewResourceId, List<Category> categoryList) {

        this.itemList = categoryList;
        this.viewResource = viewResourceId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Category getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getPosition(Category category) {
        return itemList.indexOf(category);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomViewHeader(position,parent);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, parent);
    }

    /**
     * Devuelve una fila del spinner.
     *
     * @param position posición en la lista.
     * @param parent vista padre.
     * @return vista de la fila de la lista.
     */
    public View getCustomView(int position, ViewGroup parent) {

        View row = inflater.inflate(viewResource, parent, false);

        Category category = itemList.get(position);
        ImageView icon = (ImageView) row.findViewById(R.id.imageView_category);
        TextView label = (TextView) row.findViewById(R.id.item_category);

        label.setText(category.getName());
        icon.setImageResource(Utils.getCategoryIcon(category.getId()));

        return row;
    }

    /**
     * Devueleve la cabecera del spinner.
     *
     * @param position posición.
     * @param parent vista padre.
     * @return vista de la cabecera de la lista.
     */
    public View getCustomViewHeader(int position, ViewGroup parent) {


        View row = inflater.inflate(R.layout.header_category, parent, false);

        Category category = itemList.get(position);
        ImageView icon = (ImageView) row.findViewById(R.id.imageView_category);
        TextView label = (TextView) row.findViewById(R.id.item_category);

        label.setText(category.getName());
        icon.setImageResource(Utils.getCategoryIcon(category.getId()));

        return row;
    }
}
