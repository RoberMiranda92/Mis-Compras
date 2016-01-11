package com.ubu.miscompras.model.interactors;

import android.os.Build;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by RobertoMiranda on 8/1/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class ProductGetterIteratorByDateTest {

    private DataBaseHelper helper;
    private Dao<ProductLine, Integer> lineaProductosDao;
    private Category category;
    private List<ProductLine> productLines;
    private List<ProductLine> productByDate;
    private Date startDate;
    private Date endDate;

    @Before
    public void setUp() {
        try {

            Calendar cal = Calendar.getInstance();
            productByDate = new ArrayList<>();
            category = new Category("Carne");
            Product p1 = new Product("Jamon", category);
            Product p2 = new Product("Lomo", category);
            Product p3 = new Product("Chorizo", category);
            Product p4 = new Product("Salami", category);

            Ticket t1 = new Ticket(new Date());

            cal.add(Calendar.DATE, -1);

            Ticket t2 = new Ticket(cal.getTime());

            ProductLine linea1 = new ProductLine(t1, p1, 1, 1.0, 1.0);
            ProductLine linea2 = new ProductLine(t1, p2, 1, 2.0, 2.0);
            ProductLine linea3 = new ProductLine(t1, p3, 1, 3.0, 3.0);
            ProductLine linea4 = new ProductLine(t1, p4, 1, 4.0, 4.0);

            ProductLine linea5 = new ProductLine(t2, p1, 1, 1.0, 1.0);
            ProductLine linea6 = new ProductLine(t2, p2, 1, 2.0, 2.0);
            ProductLine linea7 = new ProductLine(t2, p3, 1, 3.0, 3.0);
            ProductLine linea8 = new ProductLine(t2, p4, 1, 4.0, 4.0);

            productLines = new ArrayList<>();
            productLines.add(linea1);
            productLines.add(linea2);
            productLines.add(linea3);
            productLines.add(linea4);

            helper = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);
            lineaProductosDao = helper.getProductLineDAO();

            lineaProductosDao.create(linea1);
            lineaProductosDao.create(linea2);
            lineaProductosDao.create(linea3);
            lineaProductosDao.create(linea4);
            lineaProductosDao.create(linea5);
            lineaProductosDao.create(linea6);
            lineaProductosDao.create(linea7);
            lineaProductosDao.create(linea8);

            cal.add(Calendar.DATE,1);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            startDate=cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);

            endDate=cal.getTime();

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
    public void productGetterByDateTest() {


        ProductGetterByDateInteractor task = new ProductGetterByDateInteractor(new IOnLoadComplete() {
            @Override
            public void showError() {
                Assert.fail("productGetterByDateTest fail");
            }

            @Override
            public void loadCompleteCategoria(List<Category> items) {

            }

            @Override
            public void loadCompleteTicketProducto(List<ProductLine> items) {
                productByDate = items;
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
        }, startDate, endDate);
        task.execute();

        Assert.assertThat("Error ProductGetterByDateTest", productByDate.size(), is(productLines.size()));
        for (int i = 0; i < productLines.size(); i++) {
            Assert.assertThat("ProductGetterByDateTest equals", productByDate.get(i), is(productLines.get(i)));
        }

    }
}
