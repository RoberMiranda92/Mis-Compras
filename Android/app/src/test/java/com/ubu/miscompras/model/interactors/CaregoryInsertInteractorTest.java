package com.ubu.miscompras.model.interactors;



import android.os.Build;

import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.presenter.OnFinishedListener;
import com.ubu.miscompras.model.interactors.CaregoryInsertInteractor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by RobertoMiranda on 8/1/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class CaregoryInsertInteractorTest {

    private Boolean testResult;
    String[] names ={"Carne","Verduras","Refrescos"};
    List<String> categories;
    private DataBaseHelper helper;
    private Dao<Categoria, Integer> categoriaDao;
    @Before
    public void setUp() {
        categories = new ArrayList<>();
        for(String name : names)
            categories.add(name);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void categoryInsertTest(){

        CaregoryInsertInteractor task = new CaregoryInsertInteractor(new OnFinishedListener() {


            @Override
            public void onFinished(Boolean result) {

                testResult=result;

            }
        },categories);
        task.execute();

        Assert.assertThat("Error categoryGetterTest",testResult,is(true));
    }
}
