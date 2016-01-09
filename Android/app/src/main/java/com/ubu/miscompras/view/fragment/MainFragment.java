package com.ubu.miscompras.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.presenter.MainFragmentPresenter;
import com.ubu.miscompras.view.activity.AddProductsActivity;
import com.ubu.miscompras.view.activity.CropActivity;
import com.ubu.miscompras.view.adapters.CategoryAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Fragment donde el usuario puede ver un resumen de los gastos por categoria,
 * tambien puede insertat nuevos tickets
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
    private TextView textView_amount;
    private Spinner CategorySpinner;
    private CategoryAdapter categoryAdapter;
    private int position = 0;
    private TextView texView_totalImport;
    private String fileName;


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


        CategorySpinner = (Spinner) mView.findViewById(R.id.spinner_categorias);
        CategorySpinner.setOnItemSelectedListener(this);

        fitChart = (FitChart) mView.findViewById(R.id.fitChart);
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(100f);

        textView_amount = (TextView) mView.findViewById(R.id.textView_Percentage);
        texView_totalImport = (TextView) mView.findViewById(R.id.textView_Total);

        presenter.onResume();

        return mView;

    }

    /**
     * Este método muestra los botones ocultos
     */
    public void openButtons() {
        if (!isButtonClick) {
            addTicket_Button.startAnimation(rotate_forward);
            addImage_Button.startAnimation(fab_open);
            addCamera_Button.startAnimation(fab_open);
            isButtonClick = true;
        }
    }

    /**
     * Este metodo esconde los botones.
     */
    public void closeButtons() {
        if (isButtonClick) {
            addTicket_Button.startAnimation(rotate_backward);
            addImage_Button.startAnimation(fab_close);
            addCamera_Button.startAnimation(fab_close);
            isButtonClick = false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.app_name));
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        this.position = sharedPref.getInt("spinnerPosition", 0);
        CategorySpinner.setSelection(position);


    }

    @Override
    public void onPause() {
        super.onPause();
        closeButtons();
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("spinnerPosition", position);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CROP_PIC:
                if (resultCode == Activity.RESULT_OK)
                    presenter.uploadFile(data.getData());
                break;
            case LOAD_IMAGE_GALLERY:
                if (resultCode == Activity.RESULT_OK)
                    startCropActivity(data.getData());
                break;
            case LOAD_IMAGE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    startCropActivity(getPhotoFileUri(fileName));
                }
                break;
        }

    }


    public void starAddProductActivity(String text) {
        Intent i = new Intent();
        i.setClass(getActivity(), AddProductsActivity.class);
        i.putExtra("productos", text);
        startActivity(i);
    }

    /**
     * Este método muestra un mensaje de error.
     *
     * @param message cadena del mensaje.
     */
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Este método muestra el dialogo de progreso en la subida del fichero.
     */
    public void showProgresBar() {
        barProgressDialog = new ProgressDialog(getContext(), R.style.MyAlertDialog);
        barProgressDialog.setTitle(getString(R.string.loading));
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setIndeterminate(false);
        barProgressDialog.setMax(100);
        barProgressDialog.setProgress(0);
        barProgressDialog.show();
    }

    /**
     * Este método esconde el dialogo de progreso de la subida del fichero.
     */
    public void hideProgressBar() {
        if (barProgressDialog != null && barProgressDialog.isShowing())
            barProgressDialog.dismiss();

    }

    /**
     * Este método coloca el porcentaje en el dialogo de progreso.
     */
    public void setProgressPercentage(Integer progres) {
        barProgressDialog.setProgress(progres);
    }


    /**
     * Este método coloca el titulo en el dialogo de progreso.
     */
    public void setProgressBarTitle(String progressBarText) {
        barProgressDialog.setTitle(progressBarText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FloattingButton_addTicket:
                if (!isButtonClick)
                    openButtons();
                else
                    closeButtons();
                break;
            case R.id.FloattingButton_addCamera:
                startCameraIntent();
                closeButtons();
                break;
            case R.id.FloattingButton_addImage:
                startGalleryIntent();
                closeButtons();
                break;
        }
    }

    /**
     * Este método lanza un intent para obtener una imagen de la galeria.
     */
    private void startGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, LOAD_IMAGE_GALLERY);

    }

    /**
     * Este método lanza un intent para obtener una imagen de la camara.
     */
    private void startCameraIntent() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        fileName = formatter.format(now.getTime()) + ".jpg";
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = getPhotoFileUri(fileName);
        if (uri != null) {
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(fileName));
            startActivityForResult(takePicture, LOAD_IMAGE_CAMERA);
        } else {
            showError("Almacenamiento no disponible");
        }
    }

    /**
     * Este método lanza la actividad de crop.
     *
     * @param imageUri uri de imagen a recortar.
     */
    private void startCropActivity(Uri imageUri) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CropActivity.class);
        intent.setData(imageUri);
        startActivityForResult(intent, CROP_PIC);
    }

    /**
     * Este método muestra la lista de catoegrias en el spinner.
     *
     * @param categories
     */
    public void setCategorias(List<Category> categories) {
        categoryAdapter = new CategoryAdapter(getContext(), R.layout.item_category, categories);
        CategorySpinner.setAdapter(categoryAdapter);
    }

    /**
     * Esté método dibuja el grafico y coloca los valores correspondientes en los texView de cantidad y porcentaje.
     *
     * @param productLines
     */
    public void drawChart(List<ProductLine> productLines) {

        fitChart.setValue(0f);
        float total = 0;
        for (ProductLine p : productLines) {

            total += p.getTotalImport();
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        double importeTotal = sharedPref.getFloat("importeTotal", total);
        if (importeTotal == 0) {
            texView_totalImport.setText(getString(R.string.format_percentage, importeTotal) + getString(R.string.percent_sign));
            fitChart.setValue((float) importeTotal);
        } else {
            double percentage = (total / importeTotal) * 100;
            texView_totalImport.setText(getString(R.string.format_percentage, percentage) + getString(R.string.percent_sign));
            fitChart.setValue((float) percentage);
        }
        textView_amount.setText(getString(R.string.format_importeTotal, total));


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.drawCharByCategoty(categoryAdapter.getItem(position));
        this.position = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Este método devuelve la uri de un fichero en el directorio de imagenes, si este no existe lo crea.
     *
     * @param fileName nombre del fichero.
     * @return
     */
    private Uri getPhotoFileUri(String fileName) {
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.app_name));
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d("exception", "failed to create directory");
            }
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    /**
     * Este método comprueba si el almacenamiento SD esta disponible
     *
     * @return true si esta disponible, falso en el caso contrario.
     */
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

}
