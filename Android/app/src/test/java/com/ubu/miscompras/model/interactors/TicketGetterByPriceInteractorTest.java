package com.ubu.miscompras.model.interactors;

import android.os.Build;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by RobertoMiranda on 8/1/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class TicketGetterByPriceInteractorTest {

    private DataBaseHelper helper;
    private Dao<Ticket, Integer> ticketDao;
    private List<Ticket> ticketListByPrice;
    private List<Ticket> ticketList;
    private double MIN_PRICE=11;
    private double MAX_PRICE=45;
    private Ticket ticket;

    @Before
    public void setUp() {
        try {

            ticketListByPrice = new ArrayList<>();
            ticketList= new ArrayList<>();

            Ticket t1 = new Ticket(new Date());
            Ticket t2 = new Ticket(new Date());
            Ticket t3 = new Ticket(new Date());

            t1.setTotal(10);
            t2.setTotal(20);
            t3.setTotal(30);

            ticketList.add(t2);
            ticketList.add(t3);


            helper = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);
            ticketDao = helper.getTicketDAO();

            ticketDao.create(t1);
            ticketDao.create(t2);
            ticketDao.create(t3);

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


        TicketGetterByPriceInteractor task = new TicketGetterByPriceInteractor(new IOnLoadComplete() {
            @Override
            public void showError() {
                Assert.fail("productGetterByDateTest fail");
            }

            @Override
            public void loadCompleteCategory(List<Category> items) {

            }

            @Override
            public void loadCompleteLine(List<ProductLine> items) {

            }

            @Override
            public void loadCompleteTicket(List<Ticket> items) {
                ticketListByPrice=items;
            }

            @Override
            public void getCategories() {

            }

            @Override
            public void onResume() {

            }
        }, MIN_PRICE, MAX_PRICE);
        task.execute();

        Assert.assertThat("Error ProductGetterByDateTest", ticketList.size(), is(ticketListByPrice.size()));
        Assert.assertThat("Error Tickets equals 1",ticketList.get(0), is(ticketListByPrice.get(0)));
        Assert.assertThat("Error Tickets equals 2",ticketList.get(1), is(ticketListByPrice.get(1)));

    }
}
