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
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.view.activity.IOnEditableItem;
import com.ubu.miscompras.view.adapters.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Diálogo que se muetra cuando se quiere editar un producto antes de guardarlo.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class EditProductDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private int position;
    private EditText editText_ammount;
    private EditText editText_name;
    private EditText editText_price;
    private CategoryAdapter categoryAdapter;
    private Category selectedCategory;

    public EditProductDialog() {

    }

    /**
     * Costructor de la calse.
     *
     * @param categories  lista de categorias del spinner
     * @param lineProduct linea de producto a editar
     * @param position    posición de la lista.
     * @return Dialogo.
     */
    public static EditProductDialog newInstance(List<Category> categories, ProductLine lineProduct, int position) {
        EditProductDialog frag = new EditProductDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("categorias", new ArrayList<Parcelable>(categories));
        args.putParcelable("producto", lineProduct);
        args.putInt("position", position);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        LayoutInflater inflater = getActivity().getLayoutInflater();

        ArrayList<Category> categories = getArguments().getParcelableArrayList("categorias");
        final ProductLine productLine = getArguments().getParcelable("producto");
        int amount = productLine.getAmount();
        String productName = productLine.getProduct().getName();
        final double price = productLine.getPrice();
        this.position = getArguments().getInt("position");


        AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom)
                .setTitle(getString(R.string.products))
                .setPositiveButton(getString(R.string.accept),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                IOnEditableItem listener = (IOnEditableItem) getActivity();
                                try {
                                    if (!editText_name.getText().toString().isEmpty()) {
                                        productLine.getProduct().setCategory(selectedCategory);
                                        productLine.getProduct().setName(editText_name.getText().toString());
                                        productLine.setAmount(Integer.parseInt(editText_ammount.getText().toString()));
                                        productLine.setPrice(Double.parseDouble(editText_price.getText().toString()));
                                        productLine.setTotalImport(productLine.getPrice() * productLine.getAmount());
                                        listener.OnEditItem(productLine, position);
                                    } else {
                                        listener.showOnEditItemError(getString(R.string.emptyFields));
                                    }
                                } catch (NumberFormatException e) {
                                    listener.showOnEditItemError(getString(R.string.emptyFields));
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

        editText_ammount = (EditText) rootView.findViewById(R.id.editText_cantidad);
        editText_name = (EditText) rootView.findViewById(R.id.editText_descripcion);
        editText_price = (EditText) rootView.findViewById(R.id.editText_Precio);

        int category_position = categoryAdapter.getPosition(productLine.getProduct().getCategory());

        if (amount != 0) {
            editText_ammount.setText("" + amount);
        }
        if (productName != null) {
            editText_name.setText(productName);
        }

        if (price != 0) {
            editText_price.setText("" + price);
        }
        if (category_position != -1) {
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
