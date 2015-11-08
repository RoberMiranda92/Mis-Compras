package com.ubu.miscompras.activity;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.ubu.miscompras.R;
import com.ubu.miscompras.utils.Constans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by RobertoMiranda on 6/11/15.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long DELAY_TIME = 300;
    private static String DATABASE_PATH = "/data/data/" + Constans.PACKAGE_NAME + "/databases/";
    private static String DATABASE_NAME = Constans.DATABASE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        boolean dbExist = checkDataBase();

        if (!checkDataBase())
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }

        startApp();

    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);

    }

    /**
     * Copia el fichero sqlite de la carpeta assets a un directorio de la aplicación
     *
     * @throws IOException
     */
    private void copyDataBase() throws IOException {

        InputStream myInput = this.getAssets().open(DATABASE_NAME);


        String outFileName = DATABASE_PATH + DATABASE_NAME;


        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);

        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase() {
        File path = new File(DATABASE_PATH);
        if (!path.exists())
            path.mkdir();

        String pathDB = DATABASE_PATH + DATABASE_NAME;
        File databaseFile = new File(pathDB);

        return databaseFile.exists() ? true : false;
    }
}

