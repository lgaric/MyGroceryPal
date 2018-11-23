package hr.foi.air.mygrocerypal.myapplication.Controller;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

//DODANO
public interface GroceryListListener {
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList);
}
