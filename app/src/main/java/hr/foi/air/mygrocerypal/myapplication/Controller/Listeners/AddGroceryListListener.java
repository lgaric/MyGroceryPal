package hr.foi.air.mygrocerypal.myapplication.Controller.Listeners;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;

public interface AddGroceryListListener {
    void storesReceived(ArrayList<StoresModel> stores);
    void productsListReceived(List<GroceryListProductsModel> productsList);
    void groceryListAddedToDatabase(boolean success, String message);
}
