package hr.foi.air.mygrocerypal.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.ActiveDelivererFragment;
import hr.foi.air.mygrocerypal.myapplication.Controller.ClientGroceryListFragment;
import hr.foi.air.mygrocerypal.myapplication.Controller.DelivererFragment;
import hr.foi.air.mygrocerypal.myapplication.Controller.LoginActivity;
import hr.foi.air.mygrocerypal.myapplication.Controller.SettingsFragment;
import hr.foi.air.mygrocerypal.myapplication.Controller.StatisticsFragment;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;

public class NavigationManager {
    private static NavigationManager instance;

    public static NavigationManager getInstance(){
        if(instance == null)
            instance = new NavigationManager();
        return instance;
    }

    private ArrayList<NavigationItem> navigationItems;
    private DrawerLayout drawerLayout;
    private AppCompatActivity activity;
    private NavigationView navigationView;
    private int dynamicGroupId;

    private NavigationManager(){
        set();
    }

    private void set(){
        navigationItems = new ArrayList<>();
        navigationItems.add(new DelivererFragment());
        navigationItems.add(new ClientGroceryListFragment());
        navigationItems.add(new StatisticsFragment());
        navigationItems.add(new SettingsFragment());
    }

    public void startMainModule(){
        NavigationItem mainModule = navigationItems != null ? navigationItems.get(0) : null;
        if (mainModule != null)
            startModule(mainModule);
    }

    public void setDrawerDependencies(AppCompatActivity activity, NavigationView navigationView,
            DrawerLayout drawerLayout, int dynamicGroupId)
    {
        this.activity = activity;
        this.navigationView = navigationView;
        this.drawerLayout = drawerLayout;
        this.dynamicGroupId = dynamicGroupId;
        setupDrawer();
    }

    private void setupDrawer()
    {
        for (int i = 0; i < navigationItems.size(); i++) {
            NavigationItem item = navigationItems.get(i);
            navigationView.getMenu()
                    .add(dynamicGroupId, i, i+1, item.getName(activity))
                    .setCheckable(true).setIcon(item.getIcon(activity));
        }
    }

    private void startModule(NavigationItem item){
        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, item.getFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void selectNavigationItem(MenuItem menuItem) {
        if (!menuItem.isChecked()) {
            menuItem.setChecked(true);
            drawerLayout.closeDrawer(GravityCompat.START);
            NavigationItem selectedItem = navigationItems.get(menuItem.getItemId());
            startModule(selectedItem);
        }
    }

    /**
     * Pitaj korisnika zeli li se odjaviti iz aplikacije
     */
    public void endOfWork(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(activity.getString(R.string.logOutQuestion));
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout();
                            }
                        })
                .setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {
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
        getInstance().set();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        activity.finish();
    }

    /**
     * Dohvati fragment koji radi s GPS-om
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void locationResult(int requestCode, int resultCode, Intent data){
        Fragment item = navigationItems.get(0).getFragment();
        if(item instanceof DelivererFragment){
            ActiveDelivererFragment frag = ((DelivererFragment)item).getActiveDelivererFragment();
            frag.onActivityResult(requestCode, resultCode, data);
        }
    }
}
