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
import android.util.Log;

import com.ubu.miscompras.presenter.CropPresenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tarea que devuelve coloca un bitmap, si es necesario se rescala.
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class RotateBitMapTask extends AsyncTask<Void, Void, Bitmap> {

    private static final int MAX_IMAGE_HEIGHT = 2144;
    private static final int MAX_IMAGE_WIDTH = 3808;

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
        Bitmap srcBitmap;
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


            is = context.getContentResolver().openInputStream(imageUri);
            if (rotatedWidth >= MAX_IMAGE_WIDTH || rotatedHeight >= MAX_IMAGE_HEIGHT) {
                srcBitmap = resizeBitmap(BitmapFactory.decodeStream(is), MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT);

            } else {
                srcBitmap = BitmapFactory.decodeStream(is);
            }
            is.close();


            if (orientation > 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                        srcBitmap.getHeight(), matrix, true);
            }
            return srcBitmap;
        } catch (FileNotFoundException e) {
            Log.d("Decode Bitmap", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.d("Decode Bitmap", e.getMessage());
            return null;
        }


    }


    public int getOrientation(Uri imageUri) {
        try {
            ExifInterface exif = new ExifInterface(imageUri.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            if (rotation == ExifInterface.ORIENTATION_UNDEFINED)
                return getRotationFromMediaStore(imageUri);
            else
                return exifToDegrees(rotation);
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
     * @return angulo de orientacion real.
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

    /**
     * Este método hace un resecalado de un bitmap.
     *
     * @param bitmap bitmap a reescalar
     * @param newWidth anchura deseada
     * @param newHeight altura deseada.
     * @return bitmap redimensionado
     */
    public Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int height = (bitmap.getHeight() * newHeight / bitmap.getWidth());
        Bitmap scale = Bitmap.createScaledBitmap(bitmap, newHeight, height, true);
        return scale;


    }
}
