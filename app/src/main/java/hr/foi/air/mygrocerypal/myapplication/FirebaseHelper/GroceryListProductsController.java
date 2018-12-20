package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;

public class GroceryListProductsController extends FirebaseBaseHelper{
    private GroceryListProductsListener groceryListProductsListener;

    public GroceryListProductsController(Fragment fragment, String groceryListKey) {
        groceryListProductsListener = (GroceryListProductsListener) fragment;
        loadGroceryProductsLists(groceryListKey);
    }

    /**
     * Ucitaj sve proizvode odabrane grocery liste
     * @param groceryListKey
     */
    public void loadGroceryProductsLists(String groceryListKey) {
        mQuery = mDatabase.getReference().child(GROCERYLISTPRODUCTSNODE).child(groceryListKey);

        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GroceryListProductsModel> groceryListProducts = new ArrayList<>();
                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    GroceryListProductsModel model = temp.getValue(GroceryListProductsModel.class);
                    model.setGrocery_list_key(temp.getKey());
                    groceryListProducts.add(model);
                }
                groceryListProductsListener.groceryListProductsReceived(groceryListProducts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
