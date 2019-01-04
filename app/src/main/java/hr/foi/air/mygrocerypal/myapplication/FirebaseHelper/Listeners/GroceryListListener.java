package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public interface GroceryListListener {
    void groceryListReceived(@Nullable ArrayList<GroceryListsModel> mGroceryList);
}
