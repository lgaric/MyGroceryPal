package com.example.filter;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * interface za filtriranje
 */
public interface FilterFragment {
    int getID();
    String getName();
    Fragment getFragment();
    void setData(ArrayList<? extends FilterableObject> listOfObjects, ObjectsFilterListener listener, ArrayList<String> dropDown);
}
