package com.ubu.miscompras.view.customViews;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.ubu.miscompras.R;
import com.ubu.miscompras.view.activity.OnEditableItem;
import com.ubu.miscompras.view.adapters.CategoryAdapter;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by RobertoMiranda on 20/12/15.
 */
public class EditProdutDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private int position;
    private EditText editText_cantidad;
    private EditText editText_descripcion;
    private EditText editText_precio;
    private CategoryAdapter categoryAdapter;
    private Categoria selectedCategory;

    public EditProdutDialog() {
        // Empty constructor required for DialogFragment
    }

    public static EditProdutDialog newInstance(List<Categoria> categories, LineaProducto producto, int position) {
        EditProdutDialog frag = new EditProdutDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("categorias", new ArrayList<Parcelable>(categories));
        args.putParcelable("producto", producto);
        args.putInt("position", position);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();

        ArrayList<Categoria> categories = getArguments().getParcelableArrayList("categorias");
        final LineaProducto producto = getArguments().getParcelable("producto");
        int cantidad = producto.getCantidad();
        String descripcion = producto.getProducto().getNombre();
        final double precio = producto.getPrecio();
        this.position = getArguments().getInt("position");


        AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom)
                .setTitle("Productos")
                .setPositiveButton(getString(R.string.accept),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                OnEditableItem listener = (OnEditableItem) getActivity();
                                try {
                                    if (!editText_descripcion.getText().toString().isEmpty()) {
                                        producto.getProducto().setCategoria(selectedCategory);
                                        producto.getProducto().setNombre(editText_descripcion.getText().toString());
                                        producto.setCantidad(Integer.parseInt(editText_cantidad.getText().toString()));
                                        producto.setPrecio(Double.parseDouble(editText_precio.getText().toString()));
                                        producto.setImporte(producto.getPrecio() * producto.getCantidad());
                                        listener.OnEditItem(producto, position);
                                    } else {
                                        listener.showOnEditItemError("El producto debe tener un nombre");
                                    }
                                } catch (NumberFormatException e) {
                                    listener.showOnEditItemError("Relle los campos");
                                }

                            }
                        }
                )
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        View rootView = inflater.inflate(R.layout.dialog_edit_product, null);

        Spinner spinner_categories = (Spinner) rootView.findViewById(R.id.spinner_categorias);
        categoryAdapter = new CategoryAdapter(getContext(), R.layout.item_category, categories);
        spinner_categories.setAdapter(categoryAdapter);
        spinner_categories.setOnItemSelectedListener(this);

        editText_cantidad = (EditText) rootView.findViewById(R.id.editText_cantidad);
        editText_descripcion = (EditText) rootView.findViewById(R.id.editText_descripcion);
        editText_precio = (EditText) rootView.findViewById(R.id.editText_Precio);

        int category_position = categoryAdapter.getPosition(producto.getProducto().getCategoria());

        if (cantidad != 0) {
            editText_cantidad.setText("" + cantidad);
        }
        if (descripcion != null) {
            editText_descripcion.setText(descripcion);
        }

        if (precio != 0) {
            editText_precio.setText("" + precio);
        }
        if(category_position!=-1) {
            spinner_categories.setSelection(category_position);
        }

        b.setView(rootView);
        return b.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCategory = categoryAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
