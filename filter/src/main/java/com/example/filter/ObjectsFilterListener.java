package com.example.filter;

import com.example.filter.FilterableObject;

import java.util.ArrayList;

public interface ObjectsFilterListener {
    void listIsFiltered(ArrayList<? extends FilterableObject> listOfObjects);
}
