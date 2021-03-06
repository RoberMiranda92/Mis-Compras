/*
*   Copyright (C) 2015 Roberto Miranda.
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.ubu.miscompras.view.activity;

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
import com.ubu.miscompras.utils.Constans;
import com.ubu.miscompras.view.fragment.MainFragment;
import com.ubu.miscompras.view.fragment.ProductFragment;
import com.ubu.miscompras.view.fragment.TicketFragment;

import java.util.HashMap;

/**
 * Activity Principal de la aplicación.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private int currentFragment = 0;
    private int selectedFragment = 0;
    private NavigationView navigationView;
    private HashMap<Integer, Fragment> stack;

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


        stack = new HashMap<>();

        stack.put(Constans.RESUMEN_POSITION_FRAGMENT, new MainFragment());
        stack.put(Constans.PODUCTS_POSITION_FRAGMENT, new ProductFragment());
        stack.put(Constans.HISTORIAL_POSITION_FRAGMENT, new TicketFragment());

        navigationView.setCheckedItem(R.id.summary);

        fragment = stack.get(Constans.RESUMEN_POSITION_FRAGMENT);
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

        if (selectedFragment == Constans.RESUMEN_POSITION_FRAGMENT) {
            super.onBackPressed();
        } else {
            fragment = stack.get(Constans.RESUMEN_POSITION_FRAGMENT);
            selectedFragment = Constans.RESUMEN_POSITION_FRAGMENT;
            navigationView.getMenu().getItem(selectedFragment).setChecked(true);
            changeFragment(fragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        fragment.onPause();
        switch (id) {
            case R.id.summary:
                fragment = stack.get(Constans.RESUMEN_POSITION_FRAGMENT);
                selectedFragment = 0;
                break;
            case R.id.products:
                fragment = stack.get(Constans.PODUCTS_POSITION_FRAGMENT);
                selectedFragment = 1;
                break;
            case R.id.tickets:
                fragment = stack.get(Constans.HISTORIAL_POSITION_FRAGMENT);
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

    /**
     * Este método cambia el fragment de la vista.
     *
     * @param fragment fragment a cambiar.
     */
    private void changeFragment(Fragment fragment) {

        if (fragment != null && currentFragment != selectedFragment) {
            currentFragment = selectedFragment;
            if (fragment.isAdded())
                fragment.onResume();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.main_fragment_container, fragment);
            ft.commit();
        }


    }

    /**
     * Este método inicia la actividad de configuración.
     */
    private void startSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }



}
