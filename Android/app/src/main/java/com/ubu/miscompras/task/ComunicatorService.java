package com.ubu.miscompras.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.ubu.miscompras.R;
import com.ubu.miscompras.activity.App;
import com.ubu.miscompras.presenter.MainFragmentPresenter;
import com.ubu.miscompras.utils.AndroidMultiPartEntity;
import com.ubu.miscompras.utils.Constans;

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
 * Created by RobertoMiranda on 2/11/15.
 */
public class ComunicatorService extends AsyncTask<String, Integer, String> {


    private MainFragmentPresenter presenter;
    private int CONNECTION_TIMEOUT = 10000;
    private int SOCKET_TIMEOUT = 30000;
    private Context context;
    private long totalSize = 0;
    private boolean error = false;


    public ComunicatorService(MainFragmentPresenter presenter) {
        this.presenter = presenter;
        this.context = App.getAppContext();
    }

    @Override
    protected String doInBackground(String... params) {
        String filePath = params[0];

        File imageFile = new File(filePath);
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
            presenter.showErrorMensage(parseStatusCode(HttpStatus.SC_REQUEST_TIMEOUT));
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
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT);
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpContext localContext = new BasicHttpContext();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            String ip = pref.getString(context.getString(R.string.pref_ip_key), context.getString(R.string.pref_ip_default));

            StringBuilder builder = new StringBuilder();

            builder.append(Constans.PROTOCOL);
            builder.append(ip);
            builder.append(Constans.PORT);
            builder.append(Constans.URL_PATH);

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
                responseString = parseStatusCode(statusCode);
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

    private String parseStatusCode(int statusCode) {

        switch (statusCode) {
            case HttpStatus.SC_REQUEST_TIMEOUT:
                return "Tiempo de conexión agotado";
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return "Error al procesar imagen";
            default:
                return "Error";
        }
    }


}
