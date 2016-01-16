package com.ubu.miscompras.model.interactors;

import android.os.Build;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.Category;
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
public class ProductGetterByPriceInteractorTest {

    private DataBaseHelper helper;
    private Dao<ProductLine, Integer> lineaProductosDao;
    private Category category;
    private List<ProductLine> productLines;
    private List<ProductLine> productByPrice;
    private double MAX_PRICE = 3.50;
    private double MIN_PRRICE = 1.50;

    @Before
    public void setUp() {
        try {
            productByPrice = new ArrayList<>();

            category = new Category("Carne");
            Product p1 = new Product("Jamon", category);
            Product p2 = new Product("Lomo", category);
            Product p3 = new Product("Chorizo", category);
            Product p4 = new Product("Salami", category);
            Ticket t = new Ticket(new Date());

            ProductLine linea1 = new ProductLine(t, p1, 1, 1.0, 1.0);
            ProductLine linea2 = new ProductLine(t, p2, 1, 2.0, 2.0);
            ProductLine linea3 = new ProductLine(t, p3, 2, 3.0, 6.0);
            ProductLine linea4 = new ProductLine(t, p4, 1, 4.0, 2.0);

            productLines = new ArrayList<>();
            productLines.add(linea2);
            productLines.add(linea3);

            helper = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);

            lineaProductosDao = helper.getProductLineDAO();

            lineaProductosDao.create(linea1);
            lineaProductosDao.create(linea2);
            lineaProductosDao.create(linea3);
            lineaProductosDao.create(linea4);

        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (helper.isOpen())
            helper.close();

        OpenHelperManager.releaseHelper();
    }

    @Test
    public void productGetterByPriceTest() {


        ProductGetterByPriceInteractor task = new ProductGetterByPriceInteractor(new IOnLoadComplete() {
            @Override
            public void showError() {
                Assert.fail("productGetterByPriceTest fail");
            }

            @Override
            public void loadCompleteCategory(List<Category> items) {

            }

            @Override
            public void loadCompleteLine(List<ProductLine> items) {
                productByPrice = items;
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
        }, MIN_PRRICE, MAX_PRICE);
        task.execute();

        Assert.assertThat("Error ProductGetterByPriceTest", productByPrice.size(), is(productLines.size()));

    }
}
