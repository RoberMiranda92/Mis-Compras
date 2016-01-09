package com.ubu.miscompras.model.interactors;

import android.os.Build;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.presenter.IOnLoadComplete;
import com.ubu.miscompras.view.activity.App;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by RobertoMiranda on 8/1/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class ProductGetterByTicketInteractorTest {

    private DataBaseHelper helper;
    private Dao<ProductLine, Integer> lineaProductosDao;
    private Ticket ticket;
    private List<ProductLine> productLinesByTicket;

    @Before
    public void setUp() {
        try {

            Category category = new Category("Carne");
            Product p1 = new Product("Jamon", category);
            Product p2 = new Product("Lomo", category);
            Ticket t1 = new Ticket(new Date());

            ProductLine linea1 = new ProductLine(t1, p1, 1, 1.0, 1.0);
            ProductLine linea2 = new ProductLine(t1, p1, 3, 1.0, 1.0);

            ProductLine linea3  = new ProductLine(t1, p2, 1, 1.0, 1.0);
            ProductLine linea4 = new ProductLine(t1, p2 ,1,1.0,1.0);


            helper = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);
            lineaProductosDao = helper.getProductLineDAO();

            lineaProductosDao.create(linea1);
            lineaProductosDao.create(linea2);
            lineaProductosDao.create(linea3);
            lineaProductosDao.create(linea4);

            ticket=t1;

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
    public void productGetterByTicketTest() {


        ProductGetterByTicketInteractor task = new ProductGetterByTicketInteractor(new IOnLoadComplete() {
            @Override
            public void showError() {
                Assert.fail("productGetterByTicketTest fail");
            }

            @Override
            public void loadCompleteCategoria(List<Category> items) {

            }

            @Override
            public void loadCompleteTicketProducto(List<ProductLine> items) {
                productLinesByTicket=items;
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
        },ticket);
        task.execute();

        Assert.assertThat("productGetterByTicketTest",productLinesByTicket.size(), is(2));
        Assert.assertThat("Cantidad linea producto 1",productLinesByTicket.get(0).getAmount(),is(4));
        Assert.assertThat("Cantidad linea producto 2",productLinesByTicket.get(1).getAmount(),is(2));

    }
}
