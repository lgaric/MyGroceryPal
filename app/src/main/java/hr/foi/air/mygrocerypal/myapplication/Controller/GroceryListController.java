package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

//SVE DODANO
public class GroceryListController {

    //PRETRAŽIVANJE
    private static final String GROCERYLISTNODE  = "grocerylists";
    private static final String STATUSATTRIBUTE = "status";

    private GroceryListListener groceryListListener;
    private FirebaseDatabase firebaseDatabase;

    public GroceryListController(Fragment fragment){
        groceryListListener = (GroceryListListener)fragment;
        loadGroceryLists(GroceryListStatus.FINISHED);
    }

    //OVU METODA SE MOŽE KORISTITI I ZA AKTIVNE GROCERYLISTE
    public void loadGroceryLists(GroceryListStatus status) {
        if (status == null)
            return;

        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();

        Query query = firebaseDatabase.getReference().child(GROCERYLISTNODE)
                .orderByChild(STATUSATTRIBUTE).equalTo(GroceryListStatus.CREATED.toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GroceryListsModel> groceryList = new ArrayList<>();

                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    GroceryListsModel model = temp.getValue(GroceryListsModel.class);
                    groceryList.add(model);
                }

                groceryListListener.groceryListReceived(groceryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
