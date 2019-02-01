package com.example.filter;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;

public class FilterManager {
    private ArrayList<FilterFragment> filterFragments;
    private int currentFragmentID = 0;

    /**
     * Konstruktor
     */
    public FilterManager(){
        setAllFilters();
    }

    /**
     * Postavi fragmente za filtriranje
     */
    private void setAllFilters(){
        if(filterFragments == null)
            filterFragments = new ArrayList<>();
        filterFragments.add(new NameFilterFragment());
        filterFragments.add(new CategoryFilterFragment());
    }

    /**
     * Izmjena fragmenata za filtriranje
     * @param list lista objekata koje filtriramo
     * @param listener fragment / activity kojem prosljedujemo filtriranu listu
     * @param dropDown punjenje spinnera opcijama
     * @return
     */
    public Fragment getNextFragment(ArrayList<? extends FilterableObject> list, ObjectsFilterListener listener, ArrayList<String> dropDown){
        if(currentFragmentID == filterFragments.size())
            currentFragmentID = 0;
        Fragment temp = filterFragments.get(currentFragmentID).getFragment();
        currentFragmentID++;
        if(temp != null)
            ((FilterFragment)temp).setData(list, listener, dropDown);
        return temp;
    }
}
