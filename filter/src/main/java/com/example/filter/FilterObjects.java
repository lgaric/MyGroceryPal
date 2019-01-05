package com.example.filter;

import android.util.Log;

import com.example.filter.Enumerators.FilterType;
import com.example.filter.Listeners.ObjectsFilterListener;

import java.util.ArrayList;

/**
 * Klasa za filtriranje objekata na temelju imena i kategorije
 * @param <T>
 */
public class FilterObjects<T extends FilterableObject> {

    /**
     * Filtriraj po imenu
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> filterListByNames(ArrayList<T> mListOfObjects, String mFilterBy) {
        if(mListOfObjects == null)
            return null;

        ArrayList<T> temp = new ArrayList<>();
        mFilterBy = mFilterBy.toLowerCase();

        try {
            for (FilterableObject object : mListOfObjects) {
                if (object.name.toLowerCase().contains(mFilterBy)) {
                    temp.add((T) object);
                }
            }
        }
        catch (Exception e){
            Log.d(this.getClass().getName(), "POGRESKA KOD CASTANJA -> filterListByNames");
        }

        return temp;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<T> filterListByCategories(ArrayList<T> mListOfObjects, String mFilterBy) {
        if(mListOfObjects == null)
            return null;

        ArrayList<T> temp = new ArrayList<>();
        try {
            for (FilterableObject object : mListOfObjects) {
                if (object.category_name.equals(mFilterBy)) {
                    temp.add((T) object);
                }
            }
        }
        catch (Exception e){
            Log.d(this.getClass().getName(), "POGRESKA KOD CASTANJA -> filterListByCategories");
        }

        return temp;
    }
}
