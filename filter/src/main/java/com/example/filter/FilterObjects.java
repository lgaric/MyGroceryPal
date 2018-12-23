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

    private ArrayList<T> mListOfObjects;
    private String mFilterBy;
    private FilterType mType;
    private ObjectsFilterListener mListener;
    private Class<T> mClassType;

    public FilterObjects(ArrayList<T> mListOfObjects, String mFilterBy, FilterType mType, ObjectsFilterListener mListener, Class<T> mClassType) {
        this.mListOfObjects = mListOfObjects;
        this.mFilterBy = mFilterBy.toLowerCase();
        this.mType = mType;
        this.mListener = mListener;
        this.mClassType = mClassType;
    }

    /**
     * Filtriraj po imenu
     * @return
     */
    @SuppressWarnings("unchecked")
    private ArrayList<T> filterListByNames() {
        if(mListOfObjects == null)
            return null;

        ArrayList<T> temp = new ArrayList<>();

        try {
            for (FilterableObject object : mListOfObjects) {
                if (object.name.toLowerCase().contains(mFilterBy)) {
                    if (mClassType.isInstance(object)) {
                        temp.add((T) object);
                    }
                }
            }
        }
        catch (Exception e){
            Log.d(this.getClass().getName(), "POGRESKA KOD CASTANJA -> filterListByNames");
        }

        return temp;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<T> filterListByCategories() {
        if(mListOfObjects == null)
            return null;

        ArrayList<T> temp = new ArrayList<>();
        try {
            for (FilterableObject object : mListOfObjects) {
                if (object.category_name.equals(mFilterBy)) {
                    if (mClassType.isInstance(object)) {
                        temp.add((T) object);
                    }
                }
            }
        }
        catch (Exception e){
            Log.d(this.getClass().getName(), "POGRESKA KOD CASTANJA -> filterListByCategories");
        }

        return temp;
    }
}
