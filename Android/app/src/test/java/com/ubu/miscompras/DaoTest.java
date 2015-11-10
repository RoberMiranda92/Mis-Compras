package com.ubu.miscompras;

import android.app.Activity;
import android.os.Build;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.table.TableUtils;
import com.ubu.miscompras.activity.App;
import com.ubu.miscompras.activity.MainActivity;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.TicketProducto;

import junit.runner.BaseTestRunner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by RobertoMiranda on 10/11/15.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class DaoTest {

    private Activity context;
    private DataBaseHelper helper;
    private Dao<Categoria, Integer> categoriaDao;
    private Dao<Producto, Integer> productoDao;
    private Dao<Ticket, Integer> ticketDao;
    private Dao<TicketProducto, Integer> ticketProductoDao;

    @Before
    public void setUp() {
        context = Robolectric.setupActivity(App.class);
        helper = OpenHelperManager.getHelper(new Activity(), DataBaseHelper.class);

        try {
            categoriaDao = helper.getCategoriaDAO();
            productoDao = helper.getProductoDAO();
            ticketDao = helper.getTicketDAO();
            ticketProductoDao = helper.getTicketProductoDAO();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {
        try {
            clearDataBase();
            helper.close();
            OpenHelperManager.releaseHelper();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearDataBase() throws SQLException {
        TableUtils.clearTable(helper.getConnectionSource(), Categoria.class);
        TableUtils.clearTable(helper.getConnectionSource(), Producto.class);
        TableUtils.clearTable(helper.getConnectionSource(), Ticket.class);
        TableUtils.clearTable(helper.getConnectionSource(), TicketProducto.class);
    }

    @Test
    public void CategoriaTest() throws Exception {
        Categoria c1 = new Categoria("Frutas");
        Categoria c2 = new Categoria("Verdura");

        categoriaDao.create(c1);
        categoriaDao.create(c2);

        List<Categoria> categorias = categoriaDao.queryForAll();
        for (Categoria c : categorias)
            categoriaDao.refresh(c);

        assertEquals("Nro de categorias incorrecto", 2, categorias.size());
        assertEquals("Categoria 1 lista error", c1, categorias.get(0));
        assertEquals("Categoria 2 lista  error", c2, categorias.get(1));

        Categoria cDao1 = categoriaDao.queryForId(1);
        Categoria cDao2 = categoriaDao.queryForId(2);

        categoriaDao.refresh(cDao1);
        categoriaDao.refresh(cDao2);

        assertEquals("Categoria 1 individual error", c1, cDao1);
        assertEquals("Categoria 2 individual error", c2, cDao2);

        c1.setNombre("Leche");
        categoriaDao.update(c1);

        Categoria cDaoUpdate = categoriaDao.queryForId(1);
        categoriaDao.refresh(cDaoUpdate);

        assertEquals("Categoria 1 update error", c1, cDaoUpdate);


        categoriaDao.deleteById(2);
        List<Categoria> categoriasBorradas = categoriaDao.queryForAll();
        assertEquals("Categoria 2 borrado error", 1, categoriasBorradas.size());


    }

    @Test
    public void ProductoTest() throws Exception {
        Categoria c1 = new Categoria("Frutas");
        Categoria c2 = new Categoria("Verdura");

        Producto p1 = new Producto("Naranja", 4, 0.98, c1);
        Producto p2 = new Producto("Judias", 2, 1.88, c2);

        productoDao.create(p1);
        productoDao.create(p2);

        List<Producto> productos = productoDao.queryForAll();

        for (Producto p : productos)
            productoDao.refresh(p);

        assertEquals("Nro de categorias incorrecto", 2, productos.size());
        assertEquals("Producto 1 lista error", p1, productos.get(0));
        assertEquals("Producto 2 lista  error", p2, productos.get(1));
        assertEquals("CategoriaProducto 1 error", c1, productos.get(0).getCategoria());
        assertEquals("CategoriaProducto 2 error", c2, productos.get(1).getCategoria());

        Producto pDao1 = productoDao.queryForId(1);
        Producto pDao2 = productoDao.queryForId(2);

        Categoria cDao1 = categoriaDao.queryForId(1);
        Categoria cDao2 = categoriaDao.queryForId(2);

        assertEquals("Producto 1 individual error", p1, pDao1);
        assertEquals("Producto 2 individual error", p2, pDao2);
        assertEquals("Categoria 1 individual error", c1, cDao1);
        assertEquals("Categoria 2 individual error", c2, cDao2);


        p1.setCategoria(c2);
        productoDao.update(p1);

        Producto pDaoUpdate = productoDao.queryForId(1);
        assertEquals("Producto 1 update error", p1, pDaoUpdate);


        categoriaDao.refresh(cDao2);
        Collection<Producto> productosCategoria = cDao2.getProductos();
        assertEquals("Productos por categoria", 2, productosCategoria.size());


        productoDao.deleteById(1);
        List<Producto> productosBorrados = productoDao.queryForAll();
        assertEquals("Categoria 2 borrado error", 1, productosBorrados.size());

    }


    @Test
    public void TiketTest() throws Exception {
        Categoria c1 = new Categoria("Frutas");
        Categoria c2 = new Categoria("Verdura");


        Producto p1 = new Producto("Naranja", 4, 0.98, c1);
        Producto p2 = new Producto("Judias", 2, 1.88, c2);

        List<Producto> productos = new ArrayList<>();
        productos.add(p1);
        productos.add(p2);


        Ticket t1 = new Ticket(new Date(), "Mercadona");

        ticketDao.refresh(t1);

        for (Producto p : productos)
            ticketProductoDao.create(new TicketProducto(t1, p));


        List<Categoria> categoriasEnBD = categoriaDao.queryForAll();
        List<Producto> productosEnBD = productoDao.queryForAll();
        List<Ticket> ticketsEnBD = ticketDao.queryForAll();
        List<Producto> productosTicket = helper.lookupProductosForTicket(t1);

        assertEquals("Nro de categorias en BD", 2, categoriasEnBD.size());
        assertEquals("Nro de productos pen BD", 2, productosEnBD.size());
        assertEquals("Nro de tickets en BDr", 1, ticketsEnBD.size());
        assertEquals("Nro de productos para ticket error", 2, productosTicket.size());

        Ticket tEquals = ticketDao.queryForId(1);

        assertEquals("Tickets distintos", t1, tEquals);


        tEquals.setEstablecimiento("Sabeco");
        ticketDao.update(tEquals);

        Ticket tUpdate = ticketDao.queryForId(1);

        assertEquals("Tickets update distintos", tEquals, tUpdate);

        ticketDao.deleteById(1);
        List<Ticket> tiketsBorrados = ticketDao.queryForAll();

        assertEquals("Nro tickets borrados error", 0, tiketsBorrados.size());
    }

}
