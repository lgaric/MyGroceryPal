package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;

public interface AddGroceryListListener {
    void storesReceived(ArrayList<StoresModel> mStores);
    void productsListReceived(List<GroceryListProductsModel> mProductsList);
    void groceryListAddedToDatabase(boolean mSuccess, String mMessage);
}
