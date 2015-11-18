package com.ubu.miscompras.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ubu.miscompras.R;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductosFragment extends Fragment {


    private static EditText botonInicio;
    private static EditText botonFin;
    private static int year_x;
    private static int month_x;
    private static int day_x;
    private static int year_y;
    private static int month_y;
    private static int day_y;
    private FloatingActionButton botonBuscar;
    private int filtro = 0;
    private LinearLayout linearFechas;
    private LinearLayout linearPrecios;
    private LinearLayout linearCategorias;
    private Spinner spinerCategorias;

    public ProductosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        year_x = calendar.get(Calendar.YEAR);
        month_x = calendar.get(Calendar.MONTH);
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        year_y = calendar.get(Calendar.YEAR);
        month_y = calendar.get(Calendar.MONTH);
        day_y = calendar.get(Calendar.DAY_OF_MONTH);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View mView = inflater.inflate(R.layout.fragment_productos, container, false);

        linearFechas = (LinearLayout) mView.findViewById(R.id.linear_fechas);
        linearPrecios = (LinearLayout) mView.findViewById(R.id.linear_precios);
        linearCategorias = (LinearLayout) mView.findViewById(R.id.linear_categorias);

        botonInicio = (EditText) mView.findViewById(R.id.editText_startDate);
        botonFin = (EditText) mView.findViewById(R.id.editText_endDate);
        botonBuscar = (FloatingActionButton) mView.findViewById(R.id.buton_search);
        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateInicioFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        botonFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFinFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });


        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProducts();
            }
        });


        botonInicio.setText(day_x + "-" + month_x + "-" + year_x);
        botonFin.setText(day_y + "-" + month_y + "-" + year_y);

        String[] categorias = new String[]{"Fruta", "Verdura", "Carne", "Cosmeticos", "Bebidas"};
        spinerCategorias = (Spinner) mView.findViewById(R.id.spinner_categorias);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, categorias);
        spinerCategorias.setAdapter(adapter);
        return mView;


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_fragmentproductos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.filters) {
            showDialog();
        }


        return super.onOptionsItemSelected(item);

    }

    private void showDialog() {

        final int[] tempSelection = new int[1];
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialog);
        CharSequence[] secuence = {"Fechas", "Precios", "Categoria"};
        dialog.setSingleChoiceItems(secuence, filtro, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSelection[0] = which;
            }
        });

        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtro = tempSelection[0];
                switchFilter(filtro);
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void switchFilter(int filtro) {

        switch (filtro) {
            case 0:
                if (linearFechas.getVisibility() == View.GONE)
                    linearFechas.setVisibility(View.VISIBLE);
                linearPrecios.setVisibility(View.GONE);
                linearCategorias.setVisibility(View.GONE);
                break;
            case 1:
                if (linearPrecios.getVisibility() == View.GONE)
                    linearPrecios.setVisibility(View.VISIBLE);
                linearFechas.setVisibility(View.GONE);
                linearCategorias.setVisibility(View.GONE);
                break;
            case 2:
                if (linearCategorias.getVisibility() == View.GONE)
                    linearCategorias.setVisibility(View.VISIBLE);
                linearPrecios.setVisibility(View.GONE);
                linearFechas.setVisibility(View.GONE);
                break;
        }
    }

    private void searchProducts() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date startDate = dateFormat.parse(year_x + "/" + month_x + "/" + day_x);
            Date endDate = dateFormat.parse(year_y + "/" + month_y + "/" + day_y);

            if (startDate.after(endDate)) {
                Toast.makeText(getContext(), "La fecha de inicio debe ser anterior a la fecha de fin", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static class SelectDateInicioFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new DatePickerDialog(getActivity(), this, year_x, month_x, day_x);
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            year_x = yy;
            month_x = mm;
            day_x = dd;
            botonInicio.setText(dd + "-" + mm + "-" + yy);
        }

    }

    public static class SelectDateFinFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new DatePickerDialog(getActivity(), this, year_y, month_y, day_y);
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            year_y = yy;
            month_y = mm;
            day_y = dd;
            botonFin.setText(dd + "-" + mm + "-" + yy);
        }

    }
}

