package com.example.filter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.filter.Enumerators.FilterType;
import com.example.filter.Listeners.ObjectsFilterListener;

import java.util.ArrayList;

/**
 * Primjer poziva -> new FilterObjects<Klasa>(listaObjekata, "lampion", FilterType.NAME, this, Klasa.class).execute()
 * Klasa zaduzena za filtriranje objekata na temelju imena i kategorije
 * @param <T>
 */
public class FilterObjects<T extends FilterableObject> extends AsyncTask<Void, Void, ArrayList<T>> {

    private ArrayList<T> mListOfObjects;
    private String mFilterBy;
    private FilterType mType;
    private ObjectsFilterListener mListener;
    private Class<T> mClassType;

    /**
     * Konstruktor
     * @param listOfObjects lista objekata koje zelimo filtrirati
     * @param filterBy string na temelju kojega filtriramo
     * @param type vrsta filtriranja
     * @param listener activity, fragment koji implementira klasu ObjectsFilterListener
     * @param classType kojeg tipa je listOfObjects tj. 1 parametar
     */
    public FilterObjects(ArrayList<T> listOfObjects, String filterBy, FilterType type, ObjectsFilterListener listener, Class<T> classType){
        this.mListOfObjects = listOfObjects;
        this.mFilterBy = filterBy.toLowerCase();
        this.mType = type;
        this.mListener = listener;
        this.mClassType = classType;
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
                    if (mClassType.isInstance(object)) {
                        temp.add((T) object);
                    }
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
                    if (mClassType.isInstance(object)) {
                        temp.add((T) object);
                    }
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
        if(mListOfObjects == null || mFilterBy == null || mType == null || mListener == null || mClassType == null)
            return false;
        return true;
    }
}
