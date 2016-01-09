package com.ubu.miscompras.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.ubu.miscompras.presenter.CropPresenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by RobertoMiranda on 9/1/16.
 */
public class RotateBitMapTask extends AsyncTask<Void, Void, Bitmap> {

    private static final int MAX_IMAGE_HEIGHT = 3872;
    private static final int MAX_IMAGE_WIDTH = 2592;

    private CropPresenter presenter;
    private Uri imageUri;
    private Context context;

    public RotateBitMapTask(CropPresenter presenter, Uri imageUri) {
        this.presenter = presenter;
        this.imageUri = imageUri;
        this.context = presenter.getContext();
    }

    @Override
    public void onPostExecute(Bitmap image) {
        presenter.onFinish(image);
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(imageUri);

            BitmapFactory.Options dbo = new BitmapFactory.Options();
            dbo.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, dbo);
            is.close();

            int rotatedWidth, rotatedHeight;
            int orientation = getOrientation(imageUri);

            if (orientation == 90 || orientation == 270) {
                rotatedWidth = dbo.outHeight;
                rotatedHeight = dbo.outWidth;
            } else {
                rotatedWidth = dbo.outWidth;
                rotatedHeight = dbo.outHeight;
            }

            Bitmap srcBitmap;
            is = context.getContentResolver().openInputStream(imageUri);
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


            if (srcBitmap.getWidth() > MAX_IMAGE_WIDTH || srcBitmap.getHeight() > MAX_IMAGE_HEIGHT) {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }


    public int getOrientation(Uri imageUri) {
        try {
            ExifInterface exif = new ExifInterface(imageUri.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            if (rotation == ExifInterface.ORIENTATION_UNDEFINED)
                return getRotationFromMediaStore(imageUri);
            else return exifToDegrees(rotation);
        } catch (IOException e) {
            return 0;
        }

    }

    /**
     * Este metodo devuelve el ángulo de orientación de una imagen.
     *
     * @param imageUri uri de la imagen.
     * @return ángulo de orientación.
     */
    private int getRotationFromMediaStore(Uri imageUri) {
        String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
        Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null);
        if (cursor == null) {
            return 0;
        }

        cursor.moveToFirst();

        int orientationColumnIndex = cursor.getColumnIndex(columns[1]);
        return cursor.getInt(orientationColumnIndex);
    }

    /**
     * Obtiene el ángulo según el ángulo del exif de la imagen.
     *
     * @param exifOrientation orientaciñon del exif
     * @return angulo de orientaciñon real.
     */
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
}
