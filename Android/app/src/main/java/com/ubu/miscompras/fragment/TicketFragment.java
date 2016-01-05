package com.ubu.miscompras.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.activity.OnItemClick;
import com.ubu.miscompras.activity.TicketDetail;
import com.ubu.miscompras.adapters.TicketShowAdapter;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.TicketFragmentPresenter;
import com.ubu.miscompras.utils.VerticalDividerItemDecorator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by RobertoMiranda on 29/12/15.
 */
public class TicketFragment extends android.support.v4.app.Fragment implements View.OnClickListener, Animation.AnimationListener, OnItemClick {


    private static EditText editTextStartDate;
    private static EditText editTextEndDate;
    private FloatingActionButton botonBuscar;
    private int filtro = 0;
    private LinearLayout linearFechas;
    private LinearLayout linearPrecios;
    private Spinner spinerCategorias;
    private Animation slide_down;
    private Animation slide_up;
    private LinearLayout currentLinearLayout;
    private AppBarLayout rectangulo;
    private TicketFragmentPresenter presenter;
    private EditText editTextMinPrice;
    private EditText editTextMaxPrice;
    private Date startDate;
    private Date endDate;
    private RecyclerView recyclerView_list;
    private TicketShowAdapter recyclerView_Adapter;
    private Animation fade_close;
    private Animation fade_open;

    public TicketFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = new TicketFragmentPresenter(this);


        final Calendar calendar = Calendar.getInstance();


        slide_down = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);

        fade_close = AnimationUtils.loadAnimation(getContext(),
                R.anim.fab_hide);

        fade_open = AnimationUtils.loadAnimation(getContext(),
                R.anim.fab_show);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_productos, container, false);

        setHasOptionsMenu(true);


        linearFechas = (LinearLayout) mView.findViewById(R.id.linear_fechas);
        linearPrecios = (LinearLayout) mView.findViewById(R.id.linear_precios);

        editTextStartDate = (EditText) mView.findViewById(R.id.editText_startDate);
        editTextEndDate = (EditText) mView.findViewById(R.id.editText_endDate);

        editTextStartDate = (EditText) mView.findViewById(R.id.editText_startDate);
        editTextEndDate = (EditText) mView.findViewById(R.id.editText_endDate);

        editTextMinPrice = (EditText) mView.findViewById(R.id.editText_minPrice);
        editTextMaxPrice = (EditText) mView.findViewById(R.id.editText_maxPrice);

        botonBuscar = (FloatingActionButton) mView.findViewById(R.id.buton_search);

        editTextStartDate.setOnClickListener(this);
        editTextEndDate.setOnClickListener(this);
        botonBuscar.setOnClickListener(this);

        rectangulo = (AppBarLayout) mView.findViewById(R.id.AppBarLayout_fragmentProductos);
        slide_up.setAnimationListener(this);
        slide_down.setAnimationListener(this);


        recyclerView_Adapter = new TicketShowAdapter(this);
        recyclerView_list = (RecyclerView) mView.findViewById(R.id.recyclerView_listProductos);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_list.addItemDecoration(new VerticalDividerItemDecorator(1, false));


        switchFilter(0);


        return mView;


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.historial));
        presenter.onResume();


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

    public void hideFilter() {
        rectangulo.startAnimation(slide_up);
    }

    public void hideButton() {
        botonBuscar.startAnimation(fade_close);
    }

    public void showButton() {

        botonBuscar.startAnimation(fade_open);
    }

    public void showfilter() {
        rectangulo.startAnimation(slide_down);
    }


    public void setStartDate(Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        this.startDate = startDate;
        editTextStartDate.setText(getString(R.string.format_date, day, month + 1, year));

    }


    public void setEndDate(Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        this.endDate = endDate;
        editTextEndDate.setText(getString(R.string.format_date, day, month + 1, year));

    }

    private void showDialog() {

        final int[] tempSelection = new int[1];
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        dialog.setTitle(getString(R.string.filterDialogTitile));
        CharSequence[] secuence = getResources().getStringArray(R.array.filterDialogTicket);
        dialog.setSingleChoiceItems(secuence, filtro, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSelection[0] = which;
            }
        });

        dialog.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtro = tempSelection[0];
                hideFilter();
                hideButton();


            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                currentLinearLayout = linearFechas;
                recyclerView_Adapter.enableDatesFilter();
                break;
            case 1:
                currentLinearLayout = linearPrecios;
                recyclerView_Adapter.enablePricesFilter();
                break;
        }
        rectangulo.setExpanded(true);
    }


    public void setTicket(List<Ticket> items) {
        recyclerView_Adapter.setProducts(items);
        recyclerView_list.setAdapter(recyclerView_Adapter);
        recyclerView_Adapter.notifyDataSetChanged();


    }

    public void hideList() {
        recyclerView_list.setVisibility(View.GONE);
    }

    public void showList() {
        recyclerView_list.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buton_search:
                hideKeyboard();
                if (currentLinearLayout == linearFechas) {
                    presenter.getProductosByDate(startDate, endDate);
                }
                if (currentLinearLayout == linearPrecios) {
                    presenter.getProductosByPrice(editTextMinPrice.getText().toString(),
                            editTextMaxPrice.getText().toString());
                }
                break;
            case R.id.editText_startDate:
                presenter.showStartDialog(startDate);
                break;

            case R.id.editText_endDate:
                presenter.showEndDateDialog(endDate);
                break;


        }

    }


    public void showStartDateDialog(Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                presenter.setStartDate(calendar.getTime());
            }
        }, year, month, day).show();


    }


    public void showEndDateDialog(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                presenter.setEndDate(calendar.getTime());
            }
        }, year, month, day).show();


    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View v) {
        int itemPosition = recyclerView_list.getChildPosition(v);

        Intent intent = new Intent();
        intent.putExtra("ticket", recyclerView_Adapter.getItemAt(itemPosition));
        intent.setClass(getActivity(), TicketDetail.class);
        startActivity(intent);
    }

    public void hideKeyboard() {

        try {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            View view = ((Activity) getContext()).getCurrentFocus();
            if (view != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

        if (animation.equals(slide_down)) {
            switchFilter(filtro);
            showButton();
            currentLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (animation.equals(fade_close)) {
            botonBuscar.setVisibility(View.GONE);
        }

        if (animation.equals(fade_open)) {
            botonBuscar.setVisibility(View.VISIBLE);
        }


        if (animation.equals(slide_up)) {
            currentLinearLayout.setVisibility(View.GONE);
            rectangulo.startAnimation(slide_down);

        }
        if (animation.equals(slide_down)) {
            rectangulo.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
