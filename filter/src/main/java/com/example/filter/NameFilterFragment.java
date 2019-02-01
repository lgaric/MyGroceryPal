package com.example.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;

/**
 * Fragment za searchview / filtriranje po imenu
 */
public class NameFilterFragment extends Fragment implements FilterFragment{

    Filter filterInterface = new NameFilter();
    SearchView searchView;
    ArrayList<? extends FilterableObject> objectsToFitler;
    ObjectsFilterListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.name_filter, container, false);
        searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        return view;
    }

    /**
     * Filtriraj listu i javi fragmentu / activyu da je lista filtrirana
     * @param text
     */
    public void filterList(String text){
        if(objectsToFitler != null) {
            ArrayList<? extends FilterableObject> temp = filterInterface.filter(objectsToFitler, text);
            if(listener != null)
                listener.listIsFiltered(temp);
        }
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    /**
     * Citanje prosljedenih podataka iz FilterManagera
     * @param listOfObjects
     * @param listener
     * @param dropDown
     */
    @Override
    public void setData(ArrayList<? extends FilterableObject> listOfObjects, ObjectsFilterListener listener, ArrayList<String> dropDown){
        this.listener = listener;
        objectsToFitler = listOfObjects;
    }
}
