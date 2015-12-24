package com.ubu.miscompras.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.presenter.SplashActivityPresenter;
import com.ubu.miscompras.utils.Constans;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by RobertoMiranda on 6/11/15.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long DELAY_TIME = 300;
    private static final String DATABASE_NAME = "db.sqlite";
    private static String DATABASE_PATH = "/data/data/" + Constans.PACKAGE_NAME + "/databases/";
    private SplashActivityPresenter presenter;
    //private static String DATABASE_NAME = Constans.DATABASE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        presenter = new SplashActivityPresenter(this);


        if (!checkDataBase()) {
            String[] categories = getResources().getStringArray(R.array.CategoryItems);
            presenter.insertCategories(new ArrayList<String>(Arrays.asList(categories)));
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


    private boolean checkDataBase() {
        File path = new File(DATABASE_PATH);
        if (!path.exists())
            path.mkdir();

        String pathDB = DATABASE_PATH + DATABASE_NAME;
        File databaseFile = new File(pathDB);

        return databaseFile.exists() ? true : false;
    }

    public void showError() {

        Toast.makeText(this, getString(R.string.errorInsert), Toast.LENGTH_SHORT).show();
    }

    public void showMessage() {

        Toast.makeText(this, getString(R.string.insertCorrect), Toast.LENGTH_SHORT).show();
    }
}

