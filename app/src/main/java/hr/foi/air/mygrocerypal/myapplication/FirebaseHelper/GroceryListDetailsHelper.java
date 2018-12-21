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
    private GroceryListDetailsListener groceryListDetailsListener;

    public GroceryListDetailsHelper(GroceryListDetailsListener listener, GroceryListsModel groceryListsModel) {
        this.context = ((Fragment)listener).getContext();
        groceryListDetailsListener = listener;
        loadGroceryListProducts(groceryListsModel);
    }

    /**
     * Ucitaj sve proizvode odabrane grocery liste
     * @param groceryListsModel
     */
    public void loadGroceryListProducts(final GroceryListsModel groceryListsModel) {
        if(isNetworkAvailable()){
            mQuery = mDatabase.getReference().child(GROCERYLISTPRODUCTSNODE).child(groceryListsModel.getGrocerylist_key());

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<GroceryListProductsModel> groceryListProducts = new ArrayList<>();
                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        GroceryListProductsModel model = temp.getValue(GroceryListProductsModel.class);
                        model.setGrocery_list_key(temp.getKey());
                        groceryListProducts.add(model);
                    }
                    if(groceryListsModel.getUser_accepted_id() != null)
                        getUserInformationGroceryList(groceryListProducts, groceryListsModel);
                    else
                        groceryListDetailsListener.groceryListDetailsReceived(null, groceryListProducts);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else
            showInternetMessageWarning();
    }

    public void getUserInformationGroceryList(final ArrayList<GroceryListProductsModel> groceryListProducts, GroceryListsModel groceryListsModel) {
        if(isNetworkAvailable()) {
            mQuery = mDatabase.getReference().child(USERNODE).child(groceryListsModel.getUser_id());

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    groceryListDetailsListener.groceryListDetailsReceived(model, groceryListProducts);
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
