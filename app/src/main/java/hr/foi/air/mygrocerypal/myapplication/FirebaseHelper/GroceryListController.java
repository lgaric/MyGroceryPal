package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public class GroceryListController extends FirebaseBaseHelper{
    private GroceryListListener groceryListListener;

    public GroceryListController(Fragment fragment){
        this.context = fragment.getContext();
        groceryListListener = (GroceryListListener)fragment;
    }

    /**
     * Dohvati sve GL-ove
     * @param status
     */
    public void loadGroceryLists(final GroceryListStatus status) {
        if (status == null)
            return;

        if(isNetworkAvailable()){
            mQuery = mDatabase.getReference().child(GROCERYLISTSNODE);

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<GroceryListsModel> groceryList = new ArrayList<>();
                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        GroceryListsModel model = temp.getValue(GroceryListsModel.class);
                        model.setGrocerylist_key(temp.getKey());
                        groceryList.add(model);
                    }
                    groceryListListener.groceryListReceived(filterList(groceryList, status));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else
            showInternetMessageWarning();
    }

    /**
     * Filtriranje u dvije skupine
     *  1. skupina ACCEPTED I CREATED
     *  2. skupina FINISHED
     * @param groceryListsModels
     * @param status
     * @return
     */
    private ArrayList<GroceryListsModel> filterList(ArrayList<GroceryListsModel> groceryListsModels, GroceryListStatus status){
        ArrayList<GroceryListsModel> temp = new ArrayList<>();

        // 1. skupina ACCEPTED I CREATED
        if(status == GroceryListStatus.ACCEPTED) {
            for (int i = 0; i < groceryListsModels.size(); i++) {
                if (groceryListsModels.get(i).getStatus() != GroceryListStatus.FINISHED)
                    temp.add(groceryListsModels.get(i));
            }
        } // 2. skupina FINISHED
        else{
            for (int i = 0; i < groceryListsModels.size(); i++) {
                if (groceryListsModels.get(i).getStatus() == GroceryListStatus.FINISHED)
                    temp.add(groceryListsModels.get(i));
            }
        }

        return temp;
    }

}
