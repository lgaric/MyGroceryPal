package hr.foi.air.mygrocerypal.myapplication.Controller.Listeners;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

//OBAVIJESTI FRAGMENT DA JE LISTA GROCERYLISTI DOHVACENA SA FIREBASE-a
public interface GroceryListListener {
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList);
}
