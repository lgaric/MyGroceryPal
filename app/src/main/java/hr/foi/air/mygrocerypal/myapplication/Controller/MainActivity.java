package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private FragmentManager mFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        DelivererFragment mDelivererFragment = new DelivererFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mDelivererFragment)
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
        mToolbar.setNavigationOnClickListener(navigationClick);
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
        TextView username = (TextView) headerView.findViewById(R.id.nav_header_username);
        TextView email = (TextView) headerView.findViewById(R.id.nav_header_email);
        username.setText(CurrentUser.currentUser.getFirst_name() + " " + CurrentUser.currentUser.getLast_name());
        email.setText(CurrentUser.currentUser.getEmail());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(getFragmentManager().getBackStackEntryCount() != 0){
            getFragmentManager().popBackStack();
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        mFragmentManager = getSupportFragmentManager();
        //FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        switch (id) {
            //Handle click on static options
            case R.id.navigation_deliverer:
                mDrawer.closeDrawer(GravityCompat.START);
                DelivererFragment mDelivererFragment = new DelivererFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mDelivererFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.navigation_settings:
                mDrawer.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.navigation_client:
                mDrawer.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ClientGroceryListFragment())
                        .addToBackStack(null)
                        .commit();
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
                CurrentUser.currentUser = null;
                startActivity(new Intent(this, LoginActivity.class));
                this.finish();
                break;
            //Handle clicks on other (dynamicaly added drawer) items
            default:
                Toast.makeText(this, "Nepotreban pritisak", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    // Hamburger and back button
    View.OnClickListener navigationClick = new View.OnClickListener() {
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


}

