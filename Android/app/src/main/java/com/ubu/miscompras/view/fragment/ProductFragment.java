package com.ubu.miscompras.view.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.presenter.ProductFragmentPresenter;
import com.ubu.miscompras.utils.VerticalDividerItemDecorator;
import com.ubu.miscompras.view.adapters.CategoryAdapter;
import com.ubu.miscompras.view.adapters.ProductsAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Fragment donde el usuario puede ver los productos adquiridos
 * según los diferentes filtros.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class ProductFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, Animation.AnimationListener {


    private static EditText editTextStartDate;
    private static EditText editTextEndDate;
    private FloatingActionButton searchButton;
    private int selected = 0;
    private LinearLayout linearDate;
    private LinearLayout linearPrice;
    private LinearLayout linearCategory;
    private Spinner categorySpinner;
    private Animation slide_down;
    private Animation slide_up;
    private LinearLayout currentLinearLayout;
    private AppBarLayout appBarLayout;
    private ProductFragmentPresenter presenter;
    private CategoryAdapter categoryAdapter;
    private int categorySelected;
    private EditText editTextMinPrice;
    private EditText editTextMaxPrice;
    private Date startDate;
    private Date endDate;
    private RecyclerView recyclerView_list;
    private ProductsAdapter recyclerView_Adapter;
    private Animation fade_close;
    private Animation fade_open;
    private boolean isDialogShowing = false;

    public final int DATE_FILTER_POSITION = 0;
    public final int PRICE_FILTER_POSITION = 1;
    public final int CATEGORY_FILTER_POSITION = 2;


    public ProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = new ProductFragmentPresenter(this);

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
        setHasOptionsMenu(true);
        View mView = inflater.inflate(R.layout.fragment_productos, container, false);


        linearDate = (LinearLayout) mView.findViewById(R.id.linear_fechas);
        linearPrice = (LinearLayout) mView.findViewById(R.id.linear_precios);
        linearCategory = (LinearLayout) mView.findViewById(R.id.linear_categorias);

        editTextStartDate = (EditText) mView.findViewById(R.id.editText_startDate);
        editTextEndDate = (EditText) mView.findViewById(R.id.editText_endDate);

        editTextStartDate = (EditText) mView.findViewById(R.id.editText_startDate);
        editTextEndDate = (EditText) mView.findViewById(R.id.editText_endDate);

        editTextMinPrice = (EditText) mView.findViewById(R.id.editText_minPrice);
        editTextMaxPrice = (EditText) mView.findViewById(R.id.editText_maxPrice);

        searchButton = (FloatingActionButton) mView.findViewById(R.id.buton_search);

        editTextStartDate.setOnClickListener(this);
        editTextEndDate.setOnClickListener(this);
        searchButton.setOnClickListener(this);


        categorySpinner = (Spinner) mView.findViewById(R.id.spinner_categorias);
        categorySpinner.setOnItemSelectedListener(this);


        appBarLayout = (AppBarLayout) mView.findViewById(R.id.AppBarLayout_fragmentProductos);
        slide_up.setAnimationListener(this);
        slide_down.setAnimationListener(this);


        recyclerView_Adapter = new ProductsAdapter(getContext());
        recyclerView_list = (RecyclerView) mView.findViewById(R.id.recyclerView_listProductos);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_list.addItemDecoration(new VerticalDividerItemDecorator(1, false));


        switchFilter(0);

        return mView;


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.products));
        presenter.onResume();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragmentproductos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.productFilters) {

            showDialog();
        }


        return super.onOptionsItemSelected(item);

    }

    /**
     * Este método esconde el panel de filtros con una animación.
     */
    public void hideFilter() {
        appBarLayout.setVisibility(View.GONE);
        appBarLayout.startAnimation(slide_up);
    }

    /**
     * Este método esconde el botón de busqueda con una animación.
     */
    public void hideButton() {
        searchButton.startAnimation(fade_close);
    }

    /**
     * Este método muestra el boton de busqueda con una animación.
     */
    public void showButton() {

        searchButton.startAnimation(fade_open);
    }

    /**
     * Este metodo muestra el panel de filtros con una animación
     */
    public void showfilter() {
        appBarLayout.setVisibility(View.VISIBLE);
        appBarLayout.startAnimation(slide_down);
    }

    /**
     * Este método coloca la fecha en el TextView de fechaInicio.
     *
     * @param startDate fecha de inicio.
     */
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

    /**
     * Este método coloca la fecha en el TextView de fechaFin.
     *
     * @param endDate fecha de fin.
     */
    public void setEndDate(Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        this.endDate = endDate;
        editTextEndDate.setText(getString(R.string.format_date, day, month + 1, year));

    }

    /**
     * Este metodo muestra el dialogo de seleción de filtros.
     */
    private void showDialog() {
        if (!isDialogShowing) {
            final int[] tempSelection = new int[1];
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
            dialog.setTitle(getString(R.string.filterDialogTitile));
            CharSequence[] secuence = getResources().getStringArray(R.array.filterDialogItems);
            dialog.setSingleChoiceItems(secuence, selected, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    tempSelection[0] = which;
                }
            });

            dialog.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selected = tempSelection[0];
                    hideFilter();
                    hideButton();
                    isDialogShowing = false;


                }
            });
            dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isDialogShowing = false;
                }
            });
            isDialogShowing = true;
            dialog.show();
        }

    }

    /**
     * Este método cambia el filtro selecionado.
     *
     * @param filter filtro selecionado.
     */
    private void switchFilter(int filter) {

        switch (filter) {
            case DATE_FILTER_POSITION:
                currentLinearLayout = linearDate;
                recyclerView_Adapter.enableDatesFilter();
                break;
            case PRICE_FILTER_POSITION:
                currentLinearLayout = linearPrice;
                recyclerView_Adapter.enablePricesFilter();
                break;
            case CATEGORY_FILTER_POSITION:
                currentLinearLayout = linearCategory;
                recyclerView_Adapter.enableCategoryFilter();
                break;
        }
        appBarLayout.setExpanded(true);
    }

    /**
     * Este método muestra una lista de categories en el filtro de categories.
     *
     * @param categories Lista de categories
     */
    public void setCategorias(List<Category> categories) {
        categoryAdapter = new CategoryAdapter(getContext(), R.layout.item_category, categories);
        categorySpinner.setAdapter(categoryAdapter);
    }

    /**
     * Este metodo muestra la lista de productos.
     *
     * @param productLines lita de productos
     */
    public void setProductLines(List<ProductLine> productLines) {
        recyclerView_Adapter.setProducts(productLines);
        recyclerView_list.setAdapter(recyclerView_Adapter);
        recyclerView_Adapter.notifyDataSetChanged();


    }

    /**
     * Este método esconde la lista.
     */
    public void hideList() {
        recyclerView_list.setVisibility(View.GONE);
    }

    /**
     * Este método muestra la lista.
     */
    public void showList() {
        recyclerView_list.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categorySelected = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buton_search:
                hideKeyboard();
                if (currentLinearLayout == linearCategory)
                    presenter.getProductosByCategoria(categoryAdapter.getItem(categorySelected));
                if (currentLinearLayout == linearDate) {
                    presenter.getProductosByDate(startDate, endDate);
                }
                if (currentLinearLayout == linearPrice) {

                    presenter.getProductosByPrice(editTextMinPrice.getText().toString(),
                            editTextMaxPrice.getText().toString());
                }
                break;
            case R.id.editText_startDate:
                showStartDateDialog(startDate);
                break;

            case R.id.editText_endDate:
                showEndDateDialog(endDate);
                break;


        }

    }

    /**
     * Este método muestra el dialogo de seleción de la fecha de inicio.
     *
     * @param date fecha selecionada.
     */
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
                setStartDate(calendar.getTime());
            }
        }, year, month, day).show();


    }

    /**
     * Este método muestra el dialogo de seleción de la fecha de fin.
     *
     * @param date fecha selecionada.
     */
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
                setEndDate(calendar.getTime());
            }
        }, year, month, day).show();


    }


    /**
     * Este método esconde el teclado.
     */
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
            switchFilter(selected);
            currentLinearLayout.setVisibility(View.VISIBLE);
            showButton();
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (animation.equals(fade_close)) {
            searchButton.setVisibility(View.GONE);
        }

        if (animation.equals(fade_open)) {
            searchButton.setVisibility(View.VISIBLE);
        }


        if (animation.equals(slide_up)) {
            currentLinearLayout.setVisibility(View.GONE);
            appBarLayout.startAnimation(slide_down);
        }

        if (animation.equals(slide_down)) {
            appBarLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    /**
     * Este método muestra el error de fechas.
     */
    public void showErrorDate() {
        Toast.makeText(getContext(), getString(R.string.errorDates), Toast.LENGTH_SHORT).show();
    }

    /**
     * Este método muestra el error de productos.
     */
    public void showPricesError() {
        Toast.makeText(getContext(), getString(R.string.errorPrices), Toast.LENGTH_SHORT).show();
    }

    /**
     * Este metodo muestra el mesaje de lista vacia.
     */
    public void showEmptyListMessage() {
        Toast.makeText(getContext(), getString(R.string.ticketEmpty), Toast.LENGTH_SHORT).show();
    }

    /**
     * Este metodo muestra el mensaje de campos vacios.
     */
    public void showEmptyFieldMessage() {

        Toast.makeText(getContext(), getString(R.string.emptyFields), Toast.LENGTH_SHORT).show();
    }
}

