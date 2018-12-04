package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.SelectProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;

public class SelectProductsController {
    private static final String PRODUCTSNODE  = "products";

    private SelectProductsListener selectProductsListener;
    private FirebaseDatabase firebaseDatabase;

    public SelectProductsController(Fragment fragment){
        selectProductsListener = (SelectProductsListener)fragment;
    }

    public void loadGroceryLists(String storeName) {
        if (storeName == null)
            return;

        if(firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();

        
        Query query = firebaseDatabase.getReference().child(PRODUCTSNODE).orderByChild("store_name").equalTo(storeName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ProductsModel> productsList = new ArrayList<>();

                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    ProductsModel product = temp.getValue(ProductsModel.class);
                    product.setProduct_key(temp.getKey());
                    productsList.add(product);
                }

                selectProductsListener.productsListReceived(productsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
