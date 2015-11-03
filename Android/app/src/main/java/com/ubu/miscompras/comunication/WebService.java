package com.ubu.miscompras.comunication;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by RobertoMiranda on 2/11/15.
 */
public class WebService extends AsyncTask<String,Integer,Boolean> {


    public WebService(){
        
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String filePath = params[0];

        File imageFile = new File(filePath);

        try{
            FileInputStream imageInFile = new FileInputStream(imageFile);
            byte imageData[] = new byte[(int) imageFile.length()];
            imageInFile.read(imageData);
            // Converting Image byte array into Base64 String
            String imageDataString = encodeImage(imageData);

            byte[] decodeImage = Base64.decode(imageDataString,Base64.DEFAULT);


            FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+"/decodeImage.jpg"));

            outputStream.write(decodeImage);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }


}
