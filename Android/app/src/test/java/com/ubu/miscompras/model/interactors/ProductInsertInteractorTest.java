package com.ubu.miscompras.model.interactors;

import android.os.Build;


import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.IOnFinishedListener;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by RobertoMiranda on 8/1/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class ProductInsertInteractorTest {


    List<ProductLine> productLines;
    private Boolean testResult=false;

    @Before
    public void setUp() {
        Category c = new Category("Carne");
        Product p = new Product("Jamon", c);
        Ticket t = new Ticket(new Date());

        ProductLine linea1 = new ProductLine(t, p, 1, 1.0, 1.0);
        ProductLine linea2 = new ProductLine(t, p, 1, 1.0, 1.0);

        productLines= new ArrayList<>();
        productLines.add(linea1);
        productLines.add(linea2);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void productInsertTest() {

        ProductInsertIterator task = new ProductInsertIterator(new IOnFinishedListener() {
            @Override
            public void onFinished(Boolean result) {
                testResult = result;
            }
        }, productLines);

        task.execute();
        Assert.assertThat("Error productInsertTest ", testResult, is(true));
    }
}
