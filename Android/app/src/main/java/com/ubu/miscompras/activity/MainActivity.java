package com.ubu.miscompras.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ubu.miscompras.R;
import com.ubu.miscompras.fragment.MainFragment;
import com.ubu.miscompras.fragment.ProductosFragment;
import com.ubu.miscompras.fragment.TicketFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private int currentFragment = 0;
    private int selectedFragment = 0;
    private NavigationView navigationView;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.summary);


        navigationView.setCheckedItem(R.id.summary);
        fragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.main_fragment_container, fragment);
        ft.commit();


    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);

        if (selectedFragment == 0) {
            super.onBackPressed();
        } else {
            fragment = new MainFragment();
            selectedFragment = 0;
            navigationView.getMenu().getItem(selectedFragment).setChecked(true);
            changeFragment(fragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.summary:
                fragment = new MainFragment();
                selectedFragment = 0;
                break;
            case R.id.products:
                fragment = new ProductosFragment();
                selectedFragment = 1;
                break;
            case R.id.tickets:
                fragment = new TicketFragment();
                selectedFragment = 2;
                break;
            case R.id.config:
                startSettingsActivity();
                break;
            default:
                fragment = null;
                Toast.makeText(this, "No implementado Aun", Toast.LENGTH_SHORT).show();

        }
        changeFragment(fragment);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment fragment) {

        if (fragment != null && currentFragment != selectedFragment) {
            currentFragment = selectedFragment;
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.main_fragment_container, fragment);
            ft.commit();
        }


    }

    private void startSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

}
