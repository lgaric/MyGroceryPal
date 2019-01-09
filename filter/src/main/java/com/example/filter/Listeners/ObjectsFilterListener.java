package com.example.filter.Listeners;

import com.example.filter.FilterableObject;

import java.util.ArrayList;

/**
 * Rezultat filtriranja je lista objekata koji nasljeduju FilterableObject
 * @param <T>
 */
public interface ObjectsFilterListener<T extends FilterableObject> {
    void listIsFiltered(ArrayList<T> filterObjects);
}
