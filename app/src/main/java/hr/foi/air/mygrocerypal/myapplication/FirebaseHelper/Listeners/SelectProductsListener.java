package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.CategoriesModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;

public interface SelectProductsListener {
    void productsListReceived(ArrayList<ProductsModel> mProductsList);
    void categoriesListReceived(ArrayList<CategoriesModel> mCategoriesList);
}
