package hr.foi.air.mygrocerypal.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;

public interface SecondNavigationItem {
    String getName(Context context);
    Fragment getFragment();
}
