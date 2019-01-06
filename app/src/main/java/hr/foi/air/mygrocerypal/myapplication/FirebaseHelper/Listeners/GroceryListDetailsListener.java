package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

/**
 * Prikazi detalje o GL-u (koje prihvatio i koji su proizvodi na GL-u)
 */
public interface GroceryListDetailsListener {
        void groceryListDetailsReceived(@Nullable  UserModel mGroceryListUser, ArrayList<GroceryListProductsModel> mGroceryListProducts);
}
