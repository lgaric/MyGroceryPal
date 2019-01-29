package com.example.filter;

import android.util.Log;

import java.util.ArrayList;

public class NameFilter implements Filter {

    /**
     * Filtriranje
     * @param listOfObjects
     * @param filterBy
     * @return
     */
    @Override
    public ArrayList<? extends FilterableObject> filter(ArrayList<? extends FilterableObject> listOfObjects, String filterBy) {
        if(listOfObjects == null || filterBy == null)
            return null;

        ArrayList<FilterableObject> temp = new ArrayList<>();
        filterBy = filterBy.toLowerCase();

        for (FilterableObject object : listOfObjects) {
            if(object.name == null)
                continue;
            if (object.name.toLowerCase().contains(filterBy)) {
                temp.add(object);
            }
        }

        return temp;
    }
}
