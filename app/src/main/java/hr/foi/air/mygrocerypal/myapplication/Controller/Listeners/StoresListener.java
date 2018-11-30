package hr.foi.air.mygrocerypal.myapplication.Controller.Listeners;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;

public interface StoresListener {
    void storesReceived(ArrayList<StoresModel> stores);
}
