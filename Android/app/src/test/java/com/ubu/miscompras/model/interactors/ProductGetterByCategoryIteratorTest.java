package com.ubu.miscompras.model.interactors;

import android.os.Build;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.presenter.IOnLoadComplete;

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
    private Dao<ProductLine, Integer> lineaProductosDao;
    private Category category;
    private List<ProductLine> productLines;
    private List<ProductLine> categoryProducts;

    @Before
    public void setUp() {
        try {
            categoryProducts = new ArrayList<>();

            category = new Category("Carne");
            Product p1 = new Product("Jamon", category);
            Product p2 = new Product("Lomo", category);
            Ticket t = new Ticket(new Date());

            ProductLine linea1 = new ProductLine(t, p1, 1, 1.0, 1.0);
            ProductLine linea2 = new ProductLine(t, p2, 1, 1.0, 1.0);

            productLines = new ArrayList<>();
            productLines.add(linea1);
            productLines.add(linea2);

            helper = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);
            lineaProductosDao = helper.getProductLineDAO();

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


        ProductGetterByCategoryInterator task = new ProductGetterByCategoryInterator(new IOnLoadComplete() {
            @Override
            public void showError() {
                    Assert.fail("productGetterByCategoryTest fail");
            }

            @Override
            public void loadCompleteCategoria(List<Category> items) {

            }

            @Override
            public void loadCompleteTicketProducto(List<ProductLine> items) {
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
        }, category);
        task.execute();

        Assert.assertThat("productGetterByCategoryTest",categoryProducts.size(), is(productLines.size()));

    }
}
