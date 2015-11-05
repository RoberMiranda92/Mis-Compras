package com.ubu.miscompras.comunication;

import android.os.AsyncTask;
import android.os.RecoverySystem;
import android.util.Base64;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by RobertoMiranda on 2/11/15.
 */
public class WebService extends AsyncTask<String, Integer, Boolean> {


    public WebService() {

    }

    @Override
    protected Boolean doInBackground(String... params) {
        String filePath = params[0];

        File imageFile = new File(filePath);

        uploadUserPhoto(imageFile);


        return null;
    }

    @SuppressWarnings("deprecation")
    public void uploadUserPhoto(File image) {
        String responseString = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            HttpPost httppost = new HttpPost("http://192.168.1.27:8080/misCompras/rest/file/upload");

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("file", new FileBody(image));
            httppost.setEntity(entity);


            HttpResponse response = httpclient.execute(httppost, localContext);
            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }
        } catch (Exception e) {
            Log.d("EXCEPTION", e.getMessage());
        }
    }


    private String inputStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        return result.toString();
    }


}
