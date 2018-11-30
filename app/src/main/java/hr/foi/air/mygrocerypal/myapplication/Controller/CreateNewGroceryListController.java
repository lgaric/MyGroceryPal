package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.StoresListener;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;

public class CreateNewGroceryListController {

    private static final String STORESNODE  = "stores";

    private StoresListener storesListener;
    private FirebaseDatabase firebaseDatabase;

    public CreateNewGroceryListController(Fragment fragment){
        storesListener = (StoresListener) fragment;
    }

    public void getAllStores(){

        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();
        Query query = firebaseDatabase.getReference().child(STORESNODE);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<StoresModel> stores = new ArrayList<>();
                for (DataSnapshot temp : dataSnapshot.getChildren()){
                    StoresModel storesModel = temp.getValue(StoresModel.class);
                    storesModel.setStore_id(temp.getKey());
                    stores.add(storesModel);
                }

                storesListener.storesReceived(stores);
                Log.d("getAllStores", "sizeStores" + Integer.toString(stores.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
