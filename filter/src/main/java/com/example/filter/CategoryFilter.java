package com.example.filter;

import android.util.Log;

import java.util.ArrayList;

public class CategoryFilter implements Filter {


    @Override
    public ArrayList<? extends FilterableObject> filter(ArrayList<? extends FilterableObject> listOfObjects, String filterBy) {
        if(listOfObjects == null)
            return null;

        ArrayList<FilterableObject> temp = new ArrayList<>();

        for (FilterableObject object : listOfObjects) {
            if(object.category_name == null)
                continue;
            if (object.category_name.equals(filterBy)) {
                temp.add(object);
            }
        }

        return temp;
    }
}
