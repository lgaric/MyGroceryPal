package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;

public interface SelectProductsListener {
    void productsListReceived(ArrayList<ProductsModel> mProductsList);
}
