package com.ubu.miscompras.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.j256.ormlite.dao.Dao;
import com.ubu.miscompras.R;
import com.ubu.miscompras.comunication.WebService;
import com.ubu.miscompras.database.DataBaseHelper;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.TicketProducto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Animation rotate_forward, rotate_backward;
    private FloatingActionButton addTicket_Button;
    private boolean isButtonClick = false;
    private FloatingActionButton addCamera_Button;
    private FloatingActionButton addImage_Button;
    private Animation fab_open;
    private Animation fab_close;

    private final int LOAD_IMAGE_GALLERY = 1;
    private final int LOAD_IMAGE_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addTicket_Button = (FloatingActionButton) findViewById(R.id.FloattingButton_addTicket);
        addCamera_Button = (FloatingActionButton) findViewById(R.id.FloattingButton_addCamera);
        addImage_Button = (FloatingActionButton) findViewById(R.id.FloattingButton_addImage);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.FloattingButton_addTicket:
                        openButtonSelector();
                        break;
                    case R.id.FloattingButton_addCamera:
                        openCameraIntent();
                        break;
                    case R.id.FloattingButton_addImage:
                        openGalleryIntent();
                        break;

                }
            }
        };


        addTicket_Button.setOnClickListener(clickListener);
        addImage_Button.setOnClickListener(clickListener);
        addCamera_Button.setOnClickListener(clickListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initData();


        DecoView decoView = (DecoView) findViewById(R.id.dynamicArcView);

        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFFFFF"))
                .setRange(0, 100, 0)
                .build();

        int backIndex = decoView.addSeries(seriesItem);

        final SeriesItem seriesItem2 = new SeriesItem.Builder(getResources().getColor(R.color.colorAccent))
                .setRange(0, 100, 0)
                .build();


        int series1Index = decoView.addSeries(seriesItem2);

        final TextView textPercentage = (TextView) findViewById(R.id.textPercentage);
        seriesItem2.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem2.getMinValue()) / (seriesItem2.getMaxValue() - seriesItem2.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        decoView.addEvent(new DecoEvent.Builder(100)
                .setIndex(backIndex)
                .build());

        decoView.addEvent(new DecoEvent.Builder(30)
                .setIndex(series1Index)
                .setDelay(2000)
                .build());

    }


    private void openButtonSelector() {
        if (!isButtonClick) {
            addTicket_Button.startAnimation(rotate_forward);
            addImage_Button.startAnimation(fab_open);
            addCamera_Button.startAnimation(fab_open);
            isButtonClick = true;

        } else {
            addTicket_Button.startAnimation(rotate_backward);
            addImage_Button.startAnimation(fab_close);
            addCamera_Button.startAnimation(fab_close);
            isButtonClick = false;

        }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openGalleryIntent() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, LOAD_IMAGE_GALLERY);

    }

    private void openCameraIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, LOAD_IMAGE_CAMERA);//zero can be replaced with any action code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOAD_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "OK GALLERY", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String picturePath = cursor.getString(columnIndex); // returns null
                    cursor.close();
                    WebService tars = new WebService();
                    tars.execute(picturePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Error GALLERY", Toast.LENGTH_SHORT).show();
                }
                break;
            case LOAD_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "OK CAMERA", Toast.LENGTH_SHORT).show();
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String picturePath = cursor.getString(columnIndex); // returns null
                    cursor.close();
                    WebService tars = new WebService();
                    tars.execute(picturePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Error CAMERA", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
