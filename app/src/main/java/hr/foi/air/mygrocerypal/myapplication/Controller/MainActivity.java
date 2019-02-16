package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import hr.foi.air.mygrocerypal.GPSLocation;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.NavigationManager;
import hr.foi.air.mygrocerypal.myapplication.PaymentHelper.PaymentActivity;
import hr.foi.air.mygrocerypal.myapplication.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        FragmentManager.OnBackStackChangedListener {
    private boolean restart;
    public static final int PAYMENT_REQUEST_CODE = 2;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    /**
     * Inicijalizacija
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.deliverer));

        mDrawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        addUserInformationToNavigation(mNavigationView);

        //Hamburger and back button
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        mToolbar.setNavigationOnClickListener(mNavigationClick);

        NavigationManager.getInstance().setDrawerDependencies(this, mNavigationView,
                mDrawer, R.id.dynamic_group);
        NavigationManager.getInstance().startMainModule();

    }

    /**
     * Promjena back stacka
     */
    @Override
    public void onBackStackChanged() {
        mDrawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
        mDrawerToggle.syncState();
    }

    /**
     * Kreiranje posta
     * @param savedInstanceState
     */
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Promjena konfiguracije
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Upisivanje Userovih informacija u navigaciju
     * @param mNavigationView
     */
    private void addUserInformationToNavigation(NavigationView mNavigationView){
        String userFullName = CurrentUser.getCurrentUser.getFirst_name() + " " + CurrentUser.getCurrentUser.getLast_name();
        View headerView = mNavigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.nav_header_username);
        TextView email = headerView.findViewById(R.id.nav_header_email);
        username.setText(userFullName);
        email.setText(CurrentUser.getCurrentUser.getEmail());
    }

    /**
     * Pritisak nazad u navigaciji
     */
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount() == 0)
                NavigationManager.getInstance().endOfWork();
            else
                super.onBackPressed();
        }
    }

    /**
     * Kreiranje options izbornika
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Odabir pojedine stavke options izbornika
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Odabir pojedine stavke navigacijskog izbornika
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.menu_about:
                //TO-DO
                break;
            case R.id.menu_logout:
                NavigationManager.getInstance().endOfWork();
                break;
            default:
                NavigationManager.getInstance().selectNavigationItem(menuItem);
                break;
        }
        return true;
    }

    /**
     * Hamburger and back button
     */
    View.OnClickListener mNavigationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
                mDrawer.openDrawer(GravityCompat.START);
            }
            else{
                onBackPressed();
            }
        }
    };

    /**
     * GPS dozvola
     * For a better experience, turn on device location...(NO THANKS / OK)
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("REQUEST_CODE", Integer.toString(requestCode));
        if (requestCode == GPSLocation.REQUEST_CHECK_SETTINGS)
            NavigationManager.getInstance().locationResult(requestCode, resultCode, data);
        else if(requestCode == PAYMENT_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                restart = true;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(restart){
            onBackPressed();
            restart = false;
        }
    }
}

