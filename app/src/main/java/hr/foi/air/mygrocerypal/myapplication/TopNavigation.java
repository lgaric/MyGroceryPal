package hr.foi.air.mygrocerypal.myapplication;

import android.support.v4.app.Fragment;

public interface TopNavigation {
    void changeFragment(Fragment item);
    void changeNavigationName(String name);
}
