package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public interface GroceryListDetailsListener {
        void groceryListDetailsReceived(@Nullable  UserModel mGroceryListUser, ArrayList<GroceryListProductsModel> mGroceryListProducts);
}
