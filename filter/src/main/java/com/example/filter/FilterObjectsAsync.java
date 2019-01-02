package com.example.filter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.filter.Enumerators.FilterType;
import com.example.filter.Listeners.ObjectsFilterListener;

import java.util.ArrayList;

/**
 *
 * Klasa zaduzena za filtriranje objekata na temelju imena i kategorije
 * @param <T>
 */
public class FilterObjectsAsync<T extends FilterableObject> extends AsyncTask<Void, Void, ArrayList<T>> {

    private ArrayList<T> mListOfObjects;
    private String mFilterBy;
    private FilterType mType;
    private ObjectsFilterListener mListener;

    /**
     * Konstruktor
     * @param listOfObjects lista objekata koje zelimo filtrirati
     * @param filterBy string na temelju kojega filtriramo
     * @param type vrsta filtriranja
     * @param listener activity, fragment koji implementira klasu ObjectsFilterListener
     */
    public FilterObjectsAsync(ArrayList<T> listOfObjects, String filterBy, FilterType type, ObjectsFilterListener listener){
        this.mListOfObjects = listOfObjects;
        this.mFilterBy = filterBy.toLowerCase();
        this.mType = type;
        this.mListener = listener;
    }

    /**
     * Filtriraj listu asinkrono
     * @param voids
     * @return
     */
    @Override
    protected ArrayList<T> doInBackground(Void... voids) {
        if(!validateInputs())
            return null;

        if(mType == FilterType.NAME)
            filterListByNames();
        else
            filterListByCategories();

        return mListOfObjects;
    }

    /**
     * Proslijedi activity, fragmentu filtriranu listu
     * @param list
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void onPostExecute(ArrayList<T> list) {
        if(list != null) {
            try {
                mListener.listIsFiltered(list);
            }
            catch (Exception e){
                Log.d(this.getClass().getName(), "POGRESKA PRILIKOM PROSLIJEDIVANJA LISTE FRAGMENTU");
            }
        }
    }

    /**
     * Filtriraj listu objekata na temelju imena
     */
    @SuppressWarnings("unchecked")
    private void filterListByNames() {
        ArrayList<T> temp = new ArrayList<>();

        try {
            for (FilterableObject object : mListOfObjects) {
                if (object.name.toLowerCase().contains(mFilterBy)) {
                    temp.add((T) object);
                }
            }

            mListOfObjects = temp;
        }
        catch (Exception e){
            Log.d(this.getClass().getName(), "POGRESKA KOD CASTANJA -> filterListByNames");
        }
    }

    /**
     * Filtriraj listu objekata na temelju kategorije
     */
    @SuppressWarnings("unchecked")
    private void filterListByCategories() {
        ArrayList<T> temp = new ArrayList<>();

        try {
            for (FilterableObject object : mListOfObjects) {
                if (object.category_name.equals(mFilterBy)) {
                    temp.add((T) object);
                }
            }

            mListOfObjects = temp;
        }
        catch (Exception e){
            Log.d(this.getClass().getName(), "POGRESKA KOD CASTANJA -> filterListByCategories");
        }
    }

    /**
     * Provjeri sve atribute ove klase
     * @return
     */
    private boolean validateInputs() {
        if(mListOfObjects == null || mFilterBy == null || mType == null || mListener == null)
            return false;
        return true;
    }
}
