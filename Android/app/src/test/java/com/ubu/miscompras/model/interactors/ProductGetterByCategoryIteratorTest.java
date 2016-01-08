package com.ubu.miscompras.model.interactors;

import android.os.Build;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.LineaProducto;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.OnLoadComplete;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by RobertoMiranda on 8/1/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class ProductGetterByCategoryIteratorTest {


    private DataBaseHelper helper;
    private Dao<LineaProducto, Integer> lineaProductosDao;
    private Categoria categoria;
    private List<LineaProducto> productLines;
    private List<LineaProducto> categoryProducts;

    @Before
    public void setUp() {
        try {
            categoryProducts = new ArrayList<>();

            categoria = new Categoria("Carne");
            Producto p1 = new Producto("Jamon", categoria);
            Producto p2 = new Producto("Lomo", categoria);
            Ticket t = new Ticket(new Date());

            LineaProducto linea1 = new LineaProducto(t, p1, 1, 1.0, 1.0);
            LineaProducto linea2 = new LineaProducto(t, p2, 1, 1.0, 1.0);

            productLines = new ArrayList<>();
            productLines.add(linea1);
            productLines.add(linea2);

            helper = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);
            lineaProductosDao = helper.getTicketProductoDAO();

            lineaProductosDao.create(linea1);
            lineaProductosDao.create(linea2);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if(helper.isOpen())
            helper.close();

        OpenHelperManager.releaseHelper();
    }

    @Test
    public void productGetterByCategoryTest() {


        ProductGetterByCategoryInterator task = new ProductGetterByCategoryInterator(new OnLoadComplete() {
            @Override
            public void showError() {
                    Assert.fail("productGetterByCategoryTest fail");
            }

            @Override
            public void loadCompleteCategoria(List<Categoria> items) {

            }

            @Override
            public void loadCompleteTicketProducto(List<LineaProducto> items) {
                categoryProducts=items;
            }

            @Override
            public void loadCompleteTicket(List<Ticket> items) {

            }

            @Override
            public void getCategories() {

            }

            @Override
            public void onResume() {

            }
        },categoria);
        task.execute();

        Assert.assertThat("productGetterByCategoryTest",categoryProducts.size(), is(productLines.size()));

    }
}
