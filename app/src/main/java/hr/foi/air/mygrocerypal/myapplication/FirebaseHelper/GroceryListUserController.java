package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListUserListener;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public class GroceryListUserController extends FirebaseBaseHelper{
    private GroceryListUserListener groceryListUserListener;

    public GroceryListUserController(Fragment fragment, String userKey) {
        this.context = fragment.getContext();
        groceryListUserListener = (GroceryListUserListener) fragment;
        if(userKey.equals("-")){
            groceryListUserListener.groceryListUserReceived(null);
        }else{
            getUserInformationGroceryList(userKey);
        }
    }

    public void getUserInformationGroceryList(String userKey) {
        if(isNetworkAvailable()) {
            mQuery = mDatabase.getReference().child(USERNODE).child(userKey);

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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
        else
            showInternetMessageWarning();
    }
}
