package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public class DelivererActiveGroceryListController {
    static final String GROCERYLISTNODE  = "grocerylists";
    static final String GROCERYLISTSTATUS = "status";

    GroceryListListener listener;
    FirebaseDatabase firebaseDatabase;

    public DelivererActiveGroceryListController(GroceryListListener listListener){
        listener = listListener;
    }

    public void loadAllActiveGroceryLists() {

        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();

        Query query = firebaseDatabase.getReference().child(GROCERYLISTNODE)
                .orderByChild(GROCERYLISTSTATUS).equalTo(GroceryListStatus.CREATED.toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GroceryListsModel> groceryList = new ArrayList<>();

                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    GroceryListsModel model = temp.getValue(GroceryListsModel.class);
                    model.setGrocerylist_key(temp.getKey());
                    groceryList.add(model);
                }
                listener.groceryListReceived(groceryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //TODO
    //1. IZBACI SVE STARIJE OD 7 DANA
    //ZATIM GLEDAJ UDALJENOST IZMEDU GROCERYLISTA I KORISNIKA
    //   a) AKO KORISNIK NEMA UKLJUCEN GPS GLEDAJ LATITUDE I LANGITUTDE IZ KLASE CURRENTUSER
    //   b) AKO IMA UKLJUCENO GLEDAJ OD HRVOJA


    //NAPOMENA !!!
    //KLASA JE JAKO SLICNA KLASI GroceryListConntroller -> POKUŠATI ISKORISTITI OVU KLASU

}
