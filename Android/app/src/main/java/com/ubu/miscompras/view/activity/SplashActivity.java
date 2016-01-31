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
package com.ubu.miscompras.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
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
 */
public class SplashActivity extends AppCompatActivity {

    private static final long DELAY_TIME = 300;
    private SplashActivityPresenter presenter;
    private static String DATABASE_NAME = Constans.DATABASE_NAME;

    public static int LOAD_STORAGE = 0;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showManual = sharedPreferences.getBoolean(getString(R.string.pref_manual_key), true);

        if (showManual) {
            Intent intent = new Intent(SplashActivity.this, ManualActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {

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

    }

    /**
     * Comprueba si el fichero de la base de datos existe.
     *
     * @return true si exite, falso si no existe.
     */
    private boolean checkDataBase() {
        File databaseFile = this.getDatabasePath(DATABASE_NAME);

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


        SharedPreferences sharedPref = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("importeTotal", total);
        editor.apply();


    }

    public void start() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        LOAD_STORAGE);
            }
        }
        new Handler().postDelayed(new TimerTask() {
            @Override
            public void run() {
                startApp();
            }
        }, 1500);
    }

}

