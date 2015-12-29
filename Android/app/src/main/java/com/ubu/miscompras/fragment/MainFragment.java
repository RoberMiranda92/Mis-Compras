package com.ubu.miscompras.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.txusballesteros.widgets.FitChart;
import com.ubu.miscompras.R;
import com.ubu.miscompras.activity.AddProductsActivity;
import com.ubu.miscompras.activity.CropActivity;
import com.ubu.miscompras.adapters.CategoryAdapter;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.presenter.MainFragmentPresenter;

import java.util.List;

/**
 * Created by RobertoMiranda on 17/11/15.
 */
public class MainFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final int CROP_PIC = 3;
    public final int LOAD_IMAGE_GALLERY = 1;
    public final int LOAD_IMAGE_CAMERA = 2;
    private Animation rotate_forward, rotate_backward;
    private FloatingActionButton addTicket_Button;
    private boolean isButtonClick = false;
    private FloatingActionButton addCamera_Button;
    private FloatingActionButton addImage_Button;
    private Animation fab_open;
    private Animation fab_close;

    private MainFragmentPresenter presenter;
    private ProgressDialog barProgressDialog;
    private FitChart fitChart;
    private TextView textView_ammount;
    private Spinner spinerCategorias;
    private CategoryAdapter categoryAdapter;
    private int position = 0;
    private TextView texView_totalImport;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        presenter = new MainFragmentPresenter(this);

        View mView = inflater.inflate(R.layout.fragment_main, container, false);


        addTicket_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addTicket);
        addCamera_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addCamera);
        addImage_Button = (FloatingActionButton) mView.findViewById(R.id.FloattingButton_addImage);

        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);


        addTicket_Button.setOnClickListener(this);
        addImage_Button.setOnClickListener(this);
        addCamera_Button.setOnClickListener(this);


        spinerCategorias = (Spinner) mView.findViewById(R.id.spinner_categorias);
        spinerCategorias.setOnItemSelectedListener(this);

        fitChart = (FitChart) mView.findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);

        textView_ammount = (TextView) mView.findViewById(R.id.textView_Percentage);
        texView_totalImport = (TextView) mView.findViewById(R.id.textView_Total);

        presenter.onResume();

        return mView;

    }


    public void openButtonSelector() {
        if (!isButtonClick) {
            addTicket_Button.startAnimation(rotate_forward);
            addImage_Button.startAnimation(fab_open);
            addCamera_Button.startAnimation(fab_open);
            isButtonClick = true;

        } else {
            addTicket_Button.startAnimation(rotate_backward);
            addImage_Button.startAnimation(fab_close);
            addCamera_Button.startAnimation(fab_close);
            isButtonClick = false;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        this.position = sharedPref.getInt("spinnerPosition", 0);
        spinerCategorias.setSelection(position);


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.app_name));


    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("spinnerPosition", position);
        editor.commit();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CROP_PIC:
                if (resultCode == Activity.RESULT_OK)
                    presenter.getProducts(data.getData());
                break;
            case LOAD_IMAGE_GALLERY:
                if (resultCode == Activity.RESULT_OK)
                    startCropActivity(data.getData());
                break;
            case LOAD_IMAGE_CAMERA:
                if (resultCode == Activity.RESULT_OK)
                    startCropActivity(data.getData());
        }

    }


    public void starAddProductActivity(String text) {
        Intent i = new Intent();
        i.setClass(getActivity(), AddProductsActivity.class);
        i.putExtra("productos", text);
        startActivity(i);
    }

    public void showError(String s) {
        if (barProgressDialog != null && barProgressDialog.isShowing())
            barProgressDialog.dismiss();
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void showProgresBar() {
        barProgressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialog);
        barProgressDialog.setTitle("Subiendo...");
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setIndeterminate(false);
        barProgressDialog.setMax(100);
        barProgressDialog.setProgress(0);
        barProgressDialog.show();


    }

    public void hideProgressBar() {
        barProgressDialog.dismiss();

    }

    public void setProgressPercentage(Integer progres) {
        barProgressDialog.setProgress(progres);
    }


    public void setProgressBarTitle(String progressBarText) {
        barProgressDialog.setTitle(progressBarText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FloattingButton_addTicket:
                openButtonSelector();
                break;
            case R.id.FloattingButton_addCamera:
                startCameraIntent();
                break;
            case R.id.FloattingButton_addImage:
                startGalleryIntent();
                break;
        }
    }

    private void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, LOAD_IMAGE_GALLERY);

    }

    private void startCameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, LOAD_IMAGE_CAMERA);
    }

    private void startCropActivity(Uri source) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CropActivity.class);
        intent.setData(source);
        startActivityForResult(intent, CROP_PIC);
    }

    public void setCategorias(List<Categoria> categorias) {
        categoryAdapter = new CategoryAdapter(getContext(), R.layout.item_category, categorias);
        spinerCategorias.setAdapter(categoryAdapter);
    }

    public void setProductLines(List<LineaProducto> productLines) {

        float total = 0;
        for (LineaProducto p : productLines) {

            total += p.getImporte();
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        double importeTotal = sharedPref.getFloat("importeTotal", total);
        if (importeTotal == 0) {
            textView_ammount.setText(getString(R.string.format_percentage, importeTotal) + getString(R.string.percent_sign));
            fitChart.setValue((float) importeTotal);
        } else {
            double percentage = (total / importeTotal) * 100;

            textView_ammount.setText(getString(R.string.format_percentage, percentage) + getString(R.string.percent_sign));
            fitChart.setValue((float) percentage);
        }
        texView_totalImport.setText(getString(R.string.format_importeTotal, total));


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.drawCharByCategoty(categoryAdapter.getItem(position));
        this.position = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
