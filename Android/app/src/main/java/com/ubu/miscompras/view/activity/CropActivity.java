package com.ubu.miscompras.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.ubu.miscompras.R;
import com.ubu.miscompras.presenter.CropPresenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Activity que muestra la imagen que se va a recortar antes de mandarla al servidor.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class CropActivity extends Activity implements View.OnClickListener {


    private CropImageView cropImageView;
    private CropPresenter presenter;
    private Uri imageUri;
    private ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstannceState) {
        super.onCreate(savedInstannceState);

        Intent data = getIntent();


        imageUri = data.getData();
        setContentView(R.layout.activity_crop);


        cropImageView = (CropImageView) findViewById(R.id.cropImageView);


        Button acceptButton = (Button) findViewById(R.id.button_recortar);
        Button cancelButton = (Button) findViewById(R.id.button_cancelar);
        acceptButton.setOnClickListener(this);

        cancelButton.setOnClickListener(this);

        presenter = new CropPresenter(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart(imageUri);

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.button_recortar) {
            Bitmap imagenRecortada = cropImageView.getCroppedBitmap();
            Uri imageUri = getImageUri(getBaseContext(), imagenRecortada);
            Intent intent = new Intent();
            intent.setData(imageUri);
            setResult(RESULT_OK, intent);
            finish();
        }
        if (id == R.id.button_cancelar) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }

    }

    /**
     * Este método coloca el bitmap en su imageView.
     *
     * @param bitmap a colocar
     */
    public void setBitmap(Bitmap bitmap) {
        cropImageView.setImageBitmap(bitmap);
    }

    /**
     * Este método muestra un mesaje de error.
     */
    public void showError() {
        Toast.makeText(this, "Imagen demasiado grande", Toast.LENGTH_SHORT).show();
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        try {
            bytes.flush();
            bytes.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.parse(path);
    }

    /**
     * Este método muestra el dialogo de progreso en la subida del fichero.
     */
    public void showProgresBar() {
        barProgressDialog = new ProgressDialog(this, R.style.MyAlertDialog);
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setIndeterminate(true);
        barProgressDialog.show();
    }

    /**
     * Este método esconde el dialogo de progreso de la subida del fichero.
     */
    public void hideProgressBar() {
        if (barProgressDialog != null && barProgressDialog.isShowing())
            barProgressDialog.dismiss();

    }

}
