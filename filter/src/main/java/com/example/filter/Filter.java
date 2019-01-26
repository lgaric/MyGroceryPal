package com.example.filter;

import java.util.ArrayList;

public interface Filter{
    ArrayList<? extends FilterableObject> filter(ArrayList<? extends FilterableObject> listOfObjects, String filterBy);
}
