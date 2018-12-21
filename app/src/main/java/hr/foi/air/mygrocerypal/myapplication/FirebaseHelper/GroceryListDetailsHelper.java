package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListDetailsListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public class GroceryListDetailsHelper extends FirebaseBaseHelper{
    private GroceryListDetailsListener mGroceryListDetailsListener;

    public GroceryListDetailsHelper(GroceryListDetailsListener mGroceryListDetailsListener) {
        this.mContext = ((Fragment)mGroceryListDetailsListener).getContext();
        this.mGroceryListDetailsListener = mGroceryListDetailsListener;
    }

    /**
     * Ucitaj sve proizvode odabrane grocery liste
     * @param mGroceryListsModel
     */
    public void loadGroceryListProducts(final GroceryListsModel mGroceryListsModel) {
        if(isNetworkAvailable()){
            mQuery = mDatabase.getReference().child(GROCERYLISTPRODUCTSNODE).child(mGroceryListsModel.getGrocerylist_key());

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<GroceryListProductsModel> groceryListProducts = new ArrayList<>();
                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        GroceryListProductsModel model = temp.getValue(GroceryListProductsModel.class);
                        model.setGrocery_list_key(temp.getKey());
                        groceryListProducts.add(model);
                    }
                    if(mGroceryListsModel.getUser_accepted_id() != null)
                        getUserInformationGroceryList(groceryListProducts, mGroceryListsModel);
                    else
                        mGroceryListDetailsListener.groceryListDetailsReceived(null, groceryListProducts);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }else
            showInternetMessageWarning();
    }

    public void getUserInformationGroceryList(final ArrayList<GroceryListProductsModel> mGroceryListProducts, GroceryListsModel mGroceryListsModel) {
        if(isNetworkAvailable()) {
            mQuery = mDatabase.getReference().child(USERNODE).child(mGroceryListsModel.getUser_id());

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    mGroceryListDetailsListener.groceryListDetailsReceived(model, mGroceryListProducts);
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
