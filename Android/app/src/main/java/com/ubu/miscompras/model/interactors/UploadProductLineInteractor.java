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
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.ubu.miscompras.R;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.presenter.AddProductsPresenter;
import com.ubu.miscompras.utils.Constans;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by RobertoMiranda on 7/1/16.
 */
public class UploadProductLineInteractor extends AsyncTask<Void, Void, Boolean> {


    private String cadena;
    private AddProductsPresenter presenter;
    private Context context;
    private boolean error = false;

    public UploadProductLineInteractor(AddProductsPresenter presenter,String cadena) {

        this.presenter = presenter;
        this.context = App.getAppContext();
        this.cadena=cadena;


    }

    @Override
    protected void onPostExecute(Boolean response) {
        if (response)
            presenter.onFinished(response);
        else
            presenter.showError("Error al subir los datos diccionario");
    }


    @Override
    protected Boolean doInBackground(Void... params) {
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
            builder.append(Constans.URL_DICCIONARIO_UPLOAD);

            HttpPost httppost = new HttpPost(builder.toString());

            StringEntity entity = new StringEntity(cadena);
            httppost.setEntity(entity);


            HttpResponse response = httpclient.execute(httppost, localContext);
            HttpEntity r_entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                return true;
            } else {
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
