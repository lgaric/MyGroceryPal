package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public class DelivererIgnoredGroceryListController extends FirebaseBaseHelper{
    GroceryListListener listener;
    GroceryListStatusListener statusListener;

    public DelivererIgnoredGroceryListController(GroceryListListener listListener, GroceryListStatusListener statusListener){
        listener = listListener;
        this.statusListener = statusListener;
    }

    /**
     * Dohvati sve kreirane GL-ove
     */
    public void loadAllIgnoredGroceryLists(){
        CurrentUser.currentUser.getIgnoredLists();
        mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                .orderByChild(GROCERYLISTSTATUSNODE).equalTo(GroceryListStatus.CREATED.toString());
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GroceryListsModel> groceryList = new ArrayList<>();
                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    GroceryListsModel model = temp.getValue(GroceryListsModel.class);
                    model.setGrocerylist_key(temp.getKey());
                    groceryList.add(model);
                }
                listener.groceryListReceived(findUserIgnoredLists(groceryList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Pronadi ignorirane liste od strane korisnika
     * @param groceryList
     * @return
     */
    public ArrayList<GroceryListsModel> findUserIgnoredLists(ArrayList<GroceryListsModel> groceryList){
        ArrayList<GroceryListsModel> temporery = new ArrayList<>();
        for(int i = 0; i < groceryList.size(); i++){
            if(CurrentUser.currentUser.getIgnoredLists().contains(groceryList.get(i).getGrocerylist_key()))
                temporery.add(groceryList.get(i));
        }
        return temporery;
    }

    public void checkGroceryListStatus(final String groceryListID, final GroceryListOperation operation) {
        mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                .child(groceryListID).child(GROCERYLISTSTATUSNODE);

        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statusListener.groceryListStatusReceived(groceryListID, dataSnapshot.getValue().toString(), operation);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                statusListener.groceryListStatusReceived(groceryListID, "", operation);
            }
        });

    }

    public String returnGroceryListFromIgnored(String grocerylist_key) {
        try {
            mDatabase.getReference().child(USERIGNOREDLISTNODE).child(CurrentUser.currentUser.getUserUID())
                    .child(grocerylist_key).removeValue();
            CurrentUser.currentUser.getIgnoredLists().remove(grocerylist_key);
            return "Lista vraćena!";
        }catch (Exception e){
            Log.e(getClass().toString(), e.getMessage());
            return "Greška prilikom ignoriranja narudžbe";
        }
    }
}
