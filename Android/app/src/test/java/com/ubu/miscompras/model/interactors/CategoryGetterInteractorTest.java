package com.ubu.miscompras.model.interactors;


import android.os.Build;


import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.BuildConfig;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.view.activity.App;
import com.ubu.miscompras.model.database.DataBaseHelper;
import com.ubu.miscompras.model.ProductLine;
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
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by RobertoMiranda on 8/1/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class CategoryGetterInteractorTest {

    String[] names ={"Carne","Verduras","Refrescos"};
    List<Category> categories;
    private DataBaseHelper helper;
    private Dao<Category, Integer> categoriaDao;
    @Before
    public void setUp() {

        helper = OpenHelperManager.getHelper(App.getAppContext(), DataBaseHelper.class);
        try {
            categoriaDao = helper.getCategoryDAO();

            for (String name : names) {
                categoriaDao.create(new Category(name));
            }
        }catch (SQLException ex){
            Assert.fail(ex.getMessage());
        }

    }

    @After
    public void tearDown() {
        if(helper.isOpen())
            helper.close();
        OpenHelperManager.releaseHelper();
    }

   @Test
    public void categoryGetterTest(){

        CategoryGetterInteractor task = new CategoryGetterInteractor(new IOnLoadComplete() {
            @Override
            public void showError() {
                Assert.fail("categoryGetterTest fail");
            }

            @Override
            public void loadCompleteCategory(List<Category> items) {
                categories=items;
            }

            @Override
            public void loadCompleteLine(List<ProductLine> items) {

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
        });
        task.execute();

        Assert.assertThat("Error categoryGetterTest",categories.size(),is(names.length));
    }
}


