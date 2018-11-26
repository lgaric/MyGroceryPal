package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public class GroceryListUserController {
    //PRETRAŽIVANJE

    private static final String USERNODE = "users";

    private GroceryListUserListener groceryListUserListener;
    private FirebaseDatabase firebaseDatabase;

    public GroceryListUserController(Fragment fragment, String userKey) {
        groceryListUserListener = (GroceryListUserListener) fragment;
        loadGroceryProductsLists(userKey);
    }

    //OVU METODA SE MOŽE KORISTITI I ZA AKTIVNE GROCERYLISTE
    public void loadGroceryProductsLists(String userKey) {

        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();

        Query query = firebaseDatabase.getReference().child(USERNODE).child(userKey);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);
                groceryListUserListener.groceryListUserReceived(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
