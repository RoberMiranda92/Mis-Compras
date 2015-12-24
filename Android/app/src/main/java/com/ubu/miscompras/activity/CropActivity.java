package com.ubu.miscompras.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.isseiaoki.simplecropview.CropImageView;
import com.ubu.miscompras.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by RobertoMiranda on 12/11/15.
 */
public class CropActivity extends Activity {


    private static final int MAX_IMAGE_DIMENSION = 9096;
    private CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstannceState) {
        super.onCreate(savedInstannceState);

        Intent data = getIntent();


        Uri imageUri = data.getData();
        setContentView(R.layout.activity_crop);


        cropImageView = (CropImageView) findViewById(R.id.cropImageView);


        Button botonRecortar = (Button) findViewById(R.id.button_recortar);
        Button botonCancelar = (Button) findViewById(R.id.button_cancelar);
        botonRecortar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap imagenRecortada = cropImageView.getCroppedBitmap();
                Uri imageUri = getImageUri(getBaseContext(), imagenRecortada);
                Intent intent = new Intent();
                intent.setData(imageUri);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        setImage(imageUri);
    }

    private void setImage(Uri imageUri) {

        Bitmap rotatedBitMap = null;
        try {
            rotatedBitMap = getCorrectlyOrientedImage(this, imageUri);
            cropImageView.setImageBitmap(rotatedBitMap);
        } catch (IOException e) {
            e.printStackTrace();
            cropImageView.setImageURI(imageUri);
        }
    }


    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    public static int getOrientation(Context context, Uri photoUri) {
    /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
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
}
