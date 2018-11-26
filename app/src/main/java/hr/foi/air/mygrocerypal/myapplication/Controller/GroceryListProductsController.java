package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;

public class GroceryListProductsController {
    //PRETRAŽIVANJE

    private static final String GROCERYLISTNODE = "grocerylistproducts";
    private static final String USERNODE = "users";

    private GroceryListProductsListener groceryListProductsListener;
    private FirebaseDatabase firebaseDatabase;

    public GroceryListProductsController(Fragment fragment, String groceryListKey) {
        groceryListProductsListener = (GroceryListProductsListener) fragment;
        loadGroceryProductsLists(groceryListKey);
    }

    //OVU METODA SE MOŽE KORISTITI I ZA AKTIVNE GROCERYLISTE
    public void loadGroceryProductsLists(String groceryListKey) {

        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();

        Query query = firebaseDatabase.getReference().child(GROCERYLISTNODE).child(groceryListKey);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
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


//                for (DataSnapshot temp : dataSnapshot.getChildren()) {
//                    GroceryListProductsModel model = new GroceryListProductsModel();
//                    model.setBought(temp.child(groceryListId).getValue(GroceryListProductsModel.class).getBought());
//                    model.setName(temp.child(groceryListId).getValue(GroceryListProductsModel.class).getName());
//                    model.setPrice(temp.child(groceryListId).getValue(GroceryListProductsModel.class).getPrice());
//                    model.setQuantity(temp.child(groceryListId).getValue(GroceryListProductsModel.class).getQuantity());
//                    groceryListProducts.add(model.getBought());
//                    groceryListProducts.add(model.getName());
//                    groceryListProducts.add(model.getPrice());
//                    groceryListProducts.add(model.getQuantity());
//                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, groceryListProducts);