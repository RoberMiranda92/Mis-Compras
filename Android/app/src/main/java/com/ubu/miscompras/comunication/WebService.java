package com.ubu.miscompras.comunication;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.ubu.miscompras.model.Imagen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by RobertoMiranda on 2/11/15.
 */
public class WebService extends AsyncTask<String, Integer, Boolean> {

    /**
     * Código de petición POST.
     */
    public static final int POST_TASK = 1;
    /**
     * Código de petición GET.
     */
    public static final int GET_TASK = 2;

    /**
     * Tiempo de conexión máximo en milisegundos
     */
    private static final int CONN_TIMEOUT = 10000;

    /**
     * Tiempo de espera de datos máximo en milisegundos
     */
    private static final int SOCKET_TIMEOUT = 5000;

    public WebService() {

    }

    @Override
    protected Boolean doInBackground(String... params) {
        String filePath = params[0];

        File imageFile = new File(filePath);

        try {
            FileInputStream imageInFile = new FileInputStream(imageFile);
            byte imageData[] = new byte[(int) imageFile.length()];
            imageInFile.read(imageData);
            String imageDataString = encodeImage(imageData);

            Imagen imagen = new Imagen(imageFile.getName(), imageDataString);

            Gson gson = new Gson();
            JSONObject request = new JSONObject(gson.toJson(imagen));

            uploadJson(request);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    private void uploadJson(JSONObject file) {
        InputStream is = null;
        OutputStream os = null;
        String response = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL("http://192.168.43.120:8080/misCompras/rest/nuevo/ticket");
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONN_TIMEOUT);
            conn.setReadTimeout(SOCKET_TIMEOUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain;");

            os = conn.getOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(os);
            String output= file.toString();
            out.write(output);
            out.flush();
            out.close();

            //conn.connect();
            if(conn.getResponseCode()!=HttpURLConnection.HTTP_OK){
                Log.d("CONN",""+conn.getResponseCode());
            }else {
                is=conn.getInputStream();
                response=inputStreamToString(is);
                Log.d("CONN",response);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();
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
