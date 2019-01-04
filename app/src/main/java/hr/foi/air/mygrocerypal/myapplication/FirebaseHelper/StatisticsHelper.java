package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;

public class StatisticsHelper extends FirebaseBaseHelper{
    private GroceryListListener mGroceryListListener;


    public StatisticsHelper(GroceryListListener mGroceryListListener) {
        this.mContext = ((Fragment)mGroceryListListener).getContext();
        this.mGroceryListListener = mGroceryListListener;
    }

    /**
     * Dohvati sve odrađene narudžbe korisnika
     */
    public void loadDeliveries() {
        if(isNetworkAvailable()) {
            mQuery = mDatabase.getReference().child(GROCERYLISTSNODE).orderByChild("user_accepted_id").equalTo(CurrentUser.getCurrentUser.getUserUID());

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<GroceryListsModel> groceryListLists = new ArrayList<>();

                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        GroceryListsModel model = temp.getValue(GroceryListsModel.class);
                        model.setGrocerylist_key(temp.getKey());
                        groceryListLists.add(model);
                    }
                    mGroceryListListener.groceryListReceived(groceryListLists);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }
        else
            showInternetMessageWarning();
    }
}
