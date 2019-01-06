package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public class GroceryListHelper extends FirebaseBaseHelper{
    private GroceryListListener mGroceryListListener;

    public GroceryListHelper(GroceryListListener mGroceryListListener){
        this.mContext = ((Fragment)mGroceryListListener).getContext();
        this.mGroceryListListener = mGroceryListListener;
    }

    /**
     * Dohvati sve GL-ove trenutnog korisnika
     * @param mGroceryListStatus
     */
    public void loadGroceryLists(final GroceryListStatus mGroceryListStatus, final String userUID) {
        if (mGroceryListStatus == null)
            return;

        if(isNetworkAvailable()){
            mQuery = mDatabase.getReference().child(GROCERYLISTSNODE).orderByChild(USERIDNODE).equalTo(userUID);

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<GroceryListsModel> groceryList = new ArrayList<>();
                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        GroceryListsModel model = temp.getValue(GroceryListsModel.class);
                        model.setGrocerylist_key(temp.getKey());
                        groceryList.add(model);
                    }
                    mGroceryListListener.groceryListReceived(filterList(groceryList, mGroceryListStatus), mGroceryListStatus);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }else
            showInternetMessageWarning();
    }

    /**
     * Filtriranje u dvije skupine
     *  1. skupina ACCEPTED I CREATED
     *  2. skupina FINISHED
     * @param mGroceryListsModels
     * @param mStatus
     * @return
     */
    private ArrayList<GroceryListsModel> filterList(ArrayList<GroceryListsModel> mGroceryListsModels, GroceryListStatus mStatus){
        ArrayList<GroceryListsModel> mFilteredList = new ArrayList<>();

        // 1. skupina ACCEPTED I CREATED
        if(mStatus == GroceryListStatus.ACCEPTED) {
            for (int i = 0; i < mGroceryListsModels.size(); i++) {
                if (mGroceryListsModels.get(i).getStatus() != GroceryListStatus.FINISHED)
                    mFilteredList.add(mGroceryListsModels.get(i));
            }
        } // 2. skupina FINISHED
        else{
            for (int i = 0; i < mGroceryListsModels.size(); i++) {
                if (mGroceryListsModels.get(i).getStatus() == GroceryListStatus.FINISHED)
                    mFilteredList.add(mGroceryListsModels.get(i));
            }
        }

        return mFilteredList;
    }

}
