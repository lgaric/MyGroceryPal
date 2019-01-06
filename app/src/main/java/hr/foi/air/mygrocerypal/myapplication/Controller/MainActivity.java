package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import java.io.InputStream;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.R;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    // Fragmenti
    private DelivererFragment mDelivererFragment;
    private SettingsFragment mSettingsFragment;
    private ClientGroceryListFragment mClientGroceryListFragment;



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

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        addUserInformationToNavigation(mNavigationView);

        //Hamburger and back button
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        mToolbar.setNavigationOnClickListener(mNavigationClick);
    }

    @Override
    public void onBackStackChanged() {
        mDrawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
        mDrawerToggle.syncState();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void addUserInformationToNavigation(NavigationView mNavigationView){
        View headerView = mNavigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.nav_header_username);
        TextView email = headerView.findViewById(R.id.nav_header_email);
        username.setText(CurrentUser.getCurrentUser.getFirst_name() + " " + CurrentUser.getCurrentUser.getLast_name());
        email.setText(CurrentUser.getCurrentUser.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getFragmentManager().getBackStackEntryCount() != 0){
            getFragmentManager().popBackStack();
        } else{
            if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof DelivererFragment)
                endOfWork();
            else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

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
            case R.id.navigation_pay:
                mDrawer.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Pritisnuli ste uplati", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_statistics:
                mDrawer.closeDrawer(GravityCompat.START);
                Toast.makeText(this, "Pritisnuli ste statistiku", Toast.LENGTH_LONG).show();
                break;
            case R.id.navigation_logout:
                mDrawer.closeDrawer(GravityCompat.START);
                logout();
                break;
            //Handle clicks on other (dynamicaly added drawer) items
            default:
                Toast.makeText(this, "Nepotreban pritisak", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    // Hamburger and back button
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
     * Ako fragment nije kreiran, kreiraj fragment i prikazi. Inace prikazi vec kreirani fragment.
     * @param newFragment
     */
    private void showFragment(Fragment newFragment){

        if(newFragment instanceof DelivererFragment){
            ClearBackStack();
            return;
        }

        FragmentManager mFragmentManager = getSupportFragmentManager();
        String fragmentClassName = newFragment.getClass().getName();
        Fragment existingFragment = mFragmentManager.findFragmentByTag(fragmentClassName);

        if(existingFragment == null){
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, newFragment, fragmentClassName)
                    .addToBackStack(null)
                    .commit();
        }else{
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, existingFragment, fragmentClassName)
                    .commit();
        }
    }

    /**
     * Brisi sve s BackStack-a osim i prikazi DelivererFragment
     */
    private void ClearBackStack(){
        while (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * Pitaj korisnika zeli li se odjaviti iz aplikacije
     */
    private void endOfWork(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Odjava?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Da",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout();
                            }
                        })

                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
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

