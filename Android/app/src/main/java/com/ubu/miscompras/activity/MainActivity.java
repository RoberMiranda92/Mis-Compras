package com.ubu.miscompras.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.fragment.MainFragment;
import com.ubu.miscompras.fragment.ProductosFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.summary);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager manager = getSupportFragmentManager();

        switch (id) {
            case R.id.summary:
                fragment = new MainFragment();
                break;
            case R.id.products:
                fragment = new ProductosFragment();
                break;
            default:
                fragment = null;
                Toast.makeText(this, "No implementado Aun", Toast.LENGTH_SHORT).show();

        }
        if (fragment != null)
            manager.beginTransaction().replace(R.id.main_fragment_container, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    private void initData() {

        /*DataBaseHelper helper = new DataBaseHelper(this);

        //InsertarCategorias

        try {
            Dao<Categoria, Integer> categoriaDAO = helper.getCategoriaDAO();
            Dao<Producto, Integer> productoDAO = helper.getProductoDAO();
            Dao<Ticket, Integer> ticketDAO = helper.getTicketDAO();
            Dao<TicketProducto, Integer> ticketProductoDAO = helper.getTicketProductoDAO();


            categoriaDAO.create(new Categoria("Fruta"));
            categoriaDAO.create(new Categoria("Verdura"));
            categoriaDAO.create(new Categoria("Lacteos"));

            List<Categoria> categorias = categoriaDAO.queryForAll();

            productoDAO.create(new Producto("Naranja", 4, 0.98, categoriaDAO.queryForId(1)));
            productoDAO.create(new Producto("Manzana", 4, 0.98, categoriaDAO.queryForId(1)));

            productoDAO.create(new Producto("Lechuga", 4, 0.98, categoriaDAO.queryForId(2)));
            productoDAO.create(new Producto("Judias", 4, 0.98, categoriaDAO.queryForId(2)));

            productoDAO.create(new Producto("Leche Entera", 4, 0.98, categoriaDAO.queryForId(3)));
            productoDAO.create(new Producto("Leche Desnatada", 4, 0.98, categoriaDAO.queryForId(3)));


            List<Producto> productos = productoDAO.queryForAll();


            ticketDAO.create(new Ticket(new Date(), "Mercadona", productos));

            List<Ticket> tickets = ticketDAO.queryForAll();

            for (Producto p : productos)
                ticketProductoDAO.create(new TicketProducto(ticketDAO.queryForId(1), p));

            List<TicketProducto> list = ticketProductoDAO.queryForAll();

            for (TicketProducto tp : list) {
                ticketDAO.refresh(tp.getTicket());
                productoDAO.refresh(tp.getProducto());
                categoriaDAO.refresh(tp.getProducto().getCategoria());
            }


            Log.d("Producto", "producto");

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }*/


    }
}
