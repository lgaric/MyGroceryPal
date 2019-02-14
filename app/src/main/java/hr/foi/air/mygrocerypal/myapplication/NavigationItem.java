package hr.foi.air.mygrocerypal.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

public interface NavigationItem {
    String getName(Context context);
    Fragment getFragment();
    Drawable getIcon(Context context);
}
