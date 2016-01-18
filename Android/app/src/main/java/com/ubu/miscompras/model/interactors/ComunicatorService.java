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
package com.ubu.miscompras.model.interactors;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import com.ubu.miscompras.R;
import com.ubu.miscompras.presenter.MainFragmentPresenter;
import com.ubu.miscompras.utils.AndroidMultiPartEntity;
import com.ubu.miscompras.utils.Constans;
import com.ubu.miscompras.view.activity.App;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;


/**
 * Tarea que se encarga de subir un fichero al servidor, y obtener su respuesta.
 * Created by RobertoMiranda on 2/11/15.
 */
public class ComunicatorService extends AsyncTask<Void, Integer, String> {


    private Uri imageUri;
    private MainFragmentPresenter presenter;
    private Context context;
    private long totalSize = 0;
    private boolean error = false;


    public ComunicatorService(MainFragmentPresenter presenter, Uri imageUri) {
        this.presenter = presenter;
        this.context = App.getAppContext();
        this.imageUri = imageUri;
    }

    @Override
    protected String doInBackground(Void... params) {

        File imageFile = new File(getRealPathFromURI(imageUri));
        if (imageFile.exists()) {
            return uploadUserPhoto(imageFile);
        } else
            return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        presenter.showProgresBar();
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            if (!error)
                presenter.onFinished(response);
            else
                presenter.showErrorMensage(response);
        } else {
            presenter.showErrorMensage(parseStatusCode(HttpStatus.SC_REQUEST_TIMEOUT, null));
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        int percentage = progress[0];
        if (percentage == 100) {
            presenter.setProgressBarTitle("Procesando...");
        }
        if (percentage != 0 && percentage != 100)
            presenter.setProgressPercentage(percentage);
    }

    @SuppressWarnings("deprecation")
    public String uploadUserPhoto(File image) {
        String responseString;
        try {
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, Constans.CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, Constans.SOCKET_TIMEOUT);
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpContext localContext = new BasicHttpContext();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            String ip = pref.getString(context.getString(R.string.pref_ip_key), context.getString(R.string.pref_ip_default));

            StringBuilder builder = new StringBuilder();

            builder.append(Constans.PROTOCOL);
            builder.append(ip);
            builder.append(Constans.PORT);
            builder.append(Constans.URL_FILE_UPLOAD);

            HttpPost httppost = new HttpPost(builder.toString());


            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
                    new AndroidMultiPartEntity.ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num / (float) totalSize) * 100));
                        }
                    });

            totalSize = image.length();
            entity.addPart("file", new FileBody(image));
            httppost.setEntity(entity);


            HttpResponse response = httpclient.execute(httppost, localContext);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                responseString = EntityUtils.toString(r_entity);
            } else {
                error = true;
                responseString = parseStatusCode(statusCode, EntityUtils.toString(r_entity));
            }
            return responseString;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseStatusCode(int statusCode, String ex) {

        switch (statusCode) {
            case HttpStatus.SC_REQUEST_TIMEOUT:
                return "Tiempo de conexión agotado";
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return ex;
            default:
                return "Error desconocido";
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        try {
            Cursor cursor = presenter.getContext().getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) {
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx); // Exception raised HERE
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
