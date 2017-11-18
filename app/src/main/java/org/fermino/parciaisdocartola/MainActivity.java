package org.fermino.parciaisdocartola;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.fermino.parciaisdocartola.pojos.MercadoStatus;
import org.fermino.parciaisdocartola.services.RestClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ProgressDialog progress;
    protected DrawerLayout drawer;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fm = getSupportFragmentManager();

        // Abre uma transação e adiciona
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainFragment, new MainFragment());
        ft.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged()
            {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null)
                {
                    Fragment currFrag = (Fragment) manager.findFragmentById(R.id.mainFragment);

                    currFrag.onResume();
                }
            }
        };
        return result;
    }


    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

   /* @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }*/

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

        if(id == R.id.nav_home){
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);*/
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainFragment, new MainFragment());
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.commit();
        }
        else if (id == R.id.nav_ligas) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainFragment, new LigaFavoritaFragment()).addToBackStack(null);
            ft.commit();
        }
        else if (id == R.id.nav_times) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.mainFragment, new TimeFavoritoFragment()).addToBackStack(null);
            ft.commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
