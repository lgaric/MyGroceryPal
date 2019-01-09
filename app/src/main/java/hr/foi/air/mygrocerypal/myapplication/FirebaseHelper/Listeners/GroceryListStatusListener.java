package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;

public interface GroceryListStatusListener extends GroceryListListener {
    void groceryListStatusReceived(String mGroceryListID, String mGroceryListStatus, GroceryListOperation mOperation);
}
