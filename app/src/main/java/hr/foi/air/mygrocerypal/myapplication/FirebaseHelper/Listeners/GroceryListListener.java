package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public interface GroceryListListener {
    void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus);
}
