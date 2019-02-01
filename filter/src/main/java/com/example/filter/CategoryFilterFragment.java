package com.example.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class CategoryFilterFragment extends Fragment implements FilterFragment {

    private ObjectsFilterListener listener;
    private ArrayList<? extends FilterableObject> objectsToFitler;
    private ArrayList<String> dropDownSpinner;
    private Spinner spinner;
    Filter filter = new CategoryFilter();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_filter, container, false);
        spinner = view.findViewById(R.id.spinner);
        fillSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void changeList(){
        String selectedItem = spinner.getSelectedItem().toString();
        if(selectedItem.equals(spinner.getItemAtPosition(0).toString()) && listener != null) {
            listener.listIsFiltered(objectsToFitler);
        }
        else {
            ArrayList<? extends FilterableObject> temp = filter.filter(objectsToFitler, selectedItem);
            if (listener != null)
                listener.listIsFiltered(temp);
        }
    }

    private void fillSpinner(){
        if(listener == null)
            return;
        ArrayAdapter<String> adapter;
        FragmentActivity activity;
        if(listener instanceof Fragment)
            activity = ((Fragment)listener).getActivity();
        else
            activity = (FragmentActivity)listener;
        adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, dropDownSpinner);
        spinner.setAdapter(adapter);
    }

    @Override
    public int getID() {
        return 1;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void setData(ArrayList<? extends FilterableObject> listOfObjects, ObjectsFilterListener listener, ArrayList<String> dropDown) {
        this.listener = listener;
        this.objectsToFitler = listOfObjects;
        this.dropDownSpinner = dropDown;
    }
}
