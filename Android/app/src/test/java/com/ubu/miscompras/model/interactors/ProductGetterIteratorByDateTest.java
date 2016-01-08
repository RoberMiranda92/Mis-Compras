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
import com.ubu.miscompras.model.interactors.ProductGetterByDateInteractor;

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
    private Dao<LineaProducto, Integer> lineaProductosDao;
    private Categoria categoria;
    private List<LineaProducto> productLines;
    private List<LineaProducto> productByDate;
    private Date startDate;
    private Date endDate;

    @Before
    public void setUp() {
        try {

            Calendar cal = Calendar.getInstance();
            productByDate = new ArrayList<>();
            categoria = new Categoria("Carne");
            Producto p1 = new Producto("Jamon", categoria);
            Producto p2 = new Producto("Lomo", categoria);
            Producto p3 = new Producto("Chorizo", categoria);
            Producto p4 = new Producto("Salami", categoria);

            Ticket t1 = new Ticket(new Date());

            cal.add(Calendar.DATE, -1);

            Ticket t2 = new Ticket(cal.getTime());

            LineaProducto linea1 = new LineaProducto(t1, p1, 1, 1.0, 1.0);
            LineaProducto linea2 = new LineaProducto(t1, p2, 1, 2.0, 2.0);
            LineaProducto linea3 = new LineaProducto(t1, p3, 1, 3.0, 2.0);
            LineaProducto linea4 = new LineaProducto(t1, p4, 1, 4.0, 2.0);

            LineaProducto linea5 = new LineaProducto(t2, p1, 1, 1.0, 1.0);
            LineaProducto linea6 = new LineaProducto(t2, p2, 1, 2.0, 2.0);
            LineaProducto linea7 = new LineaProducto(t2, p3, 1, 3.0, 2.0);
            LineaProducto linea8 = new LineaProducto(t2, p4, 1, 4.0, 2.0);

            productLines = new ArrayList<>();
            productLines.add(linea1);
            productLines.add(linea2);
            productLines.add(linea3);
            productLines.add(linea4);

            helper = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);
            lineaProductosDao = helper.getTicketProductoDAO();

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


        ProductGetterByDateInteractor task = new ProductGetterByDateInteractor(new OnLoadComplete() {
            @Override
            public void showError() {
                Assert.fail("productGetterByDateTest fail");
            }

            @Override
            public void loadCompleteCategoria(List<Categoria> items) {

            }

            @Override
            public void loadCompleteTicketProducto(List<LineaProducto> items) {
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

    }
}
