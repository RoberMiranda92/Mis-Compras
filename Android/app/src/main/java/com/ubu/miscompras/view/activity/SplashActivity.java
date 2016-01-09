package com.ubu.miscompras.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.presenter.SplashActivityPresenter;
import com.ubu.miscompras.utils.Constans;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

/**
 * Activity que muestra la imagen de splash al inicio de la aplicación.
 *
 */
public class SplashActivity extends AppCompatActivity {

    private static final long DELAY_TIME = 300;
    private SplashActivityPresenter presenter;
    private static String DATABASE_NAME = Constans.DATABASE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        presenter = new SplashActivityPresenter(this);


        if (!checkDataBase()) {
            String[] categories = getResources().getStringArray(R.array.CategoryItems);
            presenter.insertCategories(new ArrayList<>(Arrays.asList(categories)));
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
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
     * Comprueba si el fichero de la base de datos existe.
     *
     * @return true si exite, falso si no existe.
     */
    private boolean checkDataBase() {
        File  databaseFile = this.getDatabasePath(DATABASE_NAME);

        return databaseFile.exists();
    }

    public void showError() {

        Toast.makeText(this, getString(R.string.errorInsert), Toast.LENGTH_SHORT).show();
    }

    public void showMessage() {

        Toast.makeText(this, getString(R.string.insertCorrect), Toast.LENGTH_SHORT).show();
    }

    public void setTicketProducto(List<ProductLine> ticketProducto) {

        float total = 0;

        for (ProductLine l : ticketProducto)
            total += l.getTotalImport();


        SharedPreferences sharedPref =getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("importeTotal", total);
        editor.commit();

        start();


    }

    public void start()
    {
        new Handler().postDelayed(new TimerTask() {
            @Override
            public void run() {
                startApp();
            }
        },1500);
    }

}

