package com.ubu.miscompras.view.activity;

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


    private static final int MAX_IMAGE_HEIGHT = 3872;
    private static final int MAX_IMAGE_WIDTH = 2592;
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


    public Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
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
        if (rotatedWidth > MAX_IMAGE_WIDTH || rotatedHeight > MAX_IMAGE_HEIGHT) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_WIDTH);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_HEIGHT);
            float maxRatio = Math.max(widthRatio, heightRatio);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            options.inJustDecodeBounds = false;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);

        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

    /*
     * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */

        if (srcBitmap.getWidth() > MAX_IMAGE_WIDTH || srcBitmap.getHeight()> MAX_IMAGE_HEIGHT) {
            float widthRatio = ((float) srcBitmap.getWidth()) / ((float) MAX_IMAGE_WIDTH);
            float heightRatio = ((float) srcBitmap.getHeight()) / ((float) MAX_IMAGE_HEIGHT);
            float maxRatio = Math.max(widthRatio, heightRatio);

            srcBitmap = Bitmap.createScaledBitmap(srcBitmap, srcBitmap.getWidth() / (int) maxRatio,
                    srcBitmap.getHeight() / (int) maxRatio, true);
        }
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }


        return srcBitmap;
    }


    public int getOrientation(Context context, Uri imageUri) {
        try {
            ExifInterface exif = new ExifInterface(imageUri.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            if (rotation == ExifInterface.ORIENTATION_UNDEFINED)
                return getRotationFromMediaStore(context, imageUri);
            else return exifToDegrees(rotation);
        } catch (IOException e) {
            return 0;
        }

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

    public static int getRotationFromMediaStore(Context context, Uri imageUri) {
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
        if (cursor == null) {
            return 0;
        }

        cursor.moveToFirst();

        int orientationColumnIndex = cursor.getColumnIndex(columns[1]);
        return cursor.getInt(orientationColumnIndex);
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } else {
            return 0;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
