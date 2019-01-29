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

import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private boolean mClientFragmentExists = false;

    // Fragmenti
    private DelivererFragment mDelivererFragment;
    private SettingsFragment mSettingsFragment;
    private ClientGroceryListFragment mClientGroceryListFragment;
    private StatisticsFragment mStatisticsFragment;

    private int called = 0;

    /**
     * Inicijalizacija
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDelivererFragment = new DelivererFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mDelivererFragment, mDelivererFragment.getClass().getName())
                .commit();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.deliverer));

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        addUserInformationToNavigation(mNavigationView);

        //Hamburger and back button
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        mToolbar.setNavigationOnClickListener(mNavigationClick);
    }

    /**
     * Promjena back stacka
     */
    @Override
    public void onBackStackChanged() {
        if(!isMainFragment()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mDrawerToggle.syncState();

        }else{
            mDrawerToggle.setDrawerIndicatorEnabled(true);
        }
        logBackStack("Poslije");
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
     * Start
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(called == 0){
            called++;
        }
        else {
            showFragment(mDelivererFragment);
        }
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getFragmentManager().getBackStackEntryCount() > 1 && mClientFragmentExists){
            getFragmentManager().popBackStack();
        } else if(getFragmentManager().getBackStackEntryCount() > 1 && !mClientFragmentExists){
            showFragment(mDelivererFragment);
        } else if(isMainFragment())
            endOfWork();
        else
            super.onBackPressed();
        }

    /**
     * Provjera jeli trenutni fragment main fragment ili main deliverer fragment
     * @return
     */
    private boolean isMainFragment(){
        if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof DelivererFragment
                || getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ClientGroceryListFragment)
            return true;
        else return false;
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
            //Handle click on static options
            case R.id.navigation_deliverer:
                mDrawer.closeDrawer(GravityCompat.START);
                showFragment(mDelivererFragment);
                break;
            case R.id.navigation_settings:
                if(mSettingsFragment == null) mSettingsFragment = new SettingsFragment();
                mDrawer.closeDrawer(GravityCompat.START);
                showFragment(mSettingsFragment);
                break;
            case R.id.navigation_client:
                if(mClientGroceryListFragment == null) mClientGroceryListFragment = new ClientGroceryListFragment();
                mDrawer.closeDrawer(GravityCompat.START);
                showFragment(mClientGroceryListFragment);
                break;
            case R.id.navigation_statistics:
                if(mStatisticsFragment == null) mStatisticsFragment = new StatisticsFragment();
                mDrawer.closeDrawer(GravityCompat.START);
                showFragment(mStatisticsFragment);
                break;
            case R.id.navigation_logout:
                mDrawer.closeDrawer(GravityCompat.START);
                logout();
                break;
            //Handle clicks on other (dynamicaly added drawer) items
            default:
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

            if(isMainFragment()) {
                mDrawer.openDrawer(GravityCompat.START);
            }
            else{
                onBackPressed();
            }
        }
    };

    /**
     * Upisivanje poruka u Log
     * @param msg
     */
    private void logBackStack(String msg){
        Log.d("Check: Stack ---", msg);
        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++)
            Log.d("Check: ", (i+1) +  ".Fragment " + getSupportFragmentManager().getBackStackEntryAt(i).getName());

    }

    /**
     * Ako fragment nije kreiran, kreiraj fragment i prikazi. Inace prikazi vec kreirani fragment.
     * @param newFragment
     */
    public void showFragment(Fragment newFragment){

        FragmentManager mFragmentManager = getSupportFragmentManager();
        String fragmentClassName = newFragment.getClass().getSimpleName();
        String currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
        Fragment existingFragment = mFragmentManager.findFragmentByTag(fragmentClassName);

        if(currentFragment.equals(fragmentClassName))
            return;
        else if(newFragment instanceof DelivererFragment){
            mClientFragmentExists = false;
            clearBackStack();
        }else if (newFragment instanceof ClientGroceryListFragment){
            mClientFragmentExists = true;
            setClientFragment(mFragmentManager);
        }else if (existingFragment != null) {
            //ako je vec kreiran vrati se na taj fragment
            getSupportFragmentManager().popBackStack(existingFragment.getClass().getSimpleName(), 0);
        }else{
            //ako nije kreiran, kreiraj i stavi na stack
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, newFragment, fragmentClassName)
                    .addToBackStack(fragmentClassName)
                    .commit();
        }

    }

    /**
     * Brisi BackStack i postavi klijenta na pocetak
     * @param mFragmentManager
     */
    private void setClientFragment(FragmentManager mFragmentManager){
        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            clearBackStack();
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mClientGroceryListFragment, mClientGroceryListFragment.getClass().getSimpleName())
                .addToBackStack(mClientGroceryListFragment.getClass().getSimpleName())
                .commit();
    }

    /**
     * Brisi sve s BackStack-a osim i prikazi DelivererFragment
     */
    private void clearBackStack(){
        while (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * Pitaj korisnika zeli li se odjaviti iz aplikacije
     */
    private void endOfWork(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.logOutQuestion));
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout();
                            }
                        })

                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Odjava korisnika iz aplikacije i prikaz login Activitija
     */
    private void logout(){
        CurrentUser.getCurrentUser = null;
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }
}

