package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.AddGroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;

public class CreateNewGroceryListHelper extends FirebaseBaseHelper{

    private AddGroceryListListener addGroceryListListener;

    public CreateNewGroceryListHelper(AddGroceryListListener listener){
        this.context = ((Fragment)listener).getContext();
        addGroceryListListener = listener;
    }

    /**
     * Dohvati sve trgovine
     */
    public void getAllStores(){
        if(isNetworkAvailable()){
            mQuery = mDatabase.getReference().child(STORESNODE);
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<StoresModel> stores = new ArrayList<>();
                    for (DataSnapshot temp : dataSnapshot.getChildren()){
                        StoresModel storesModel = temp.getValue(StoresModel.class);
                        storesModel.setStore_id(temp.getKey());
                        stores.add(storesModel);
                    }

                    addGroceryListListener.storesReceived(stores);
                    Log.d("getAllStores", "sizeStores" + Integer.toString(stores.size()));
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
     * Napravi novi GL
     * @param groceryListsModel
     * @param groceryListProductsModels
     */
    public void saveGL_withProducts(GroceryListsModel groceryListsModel, List<GroceryListProductsModel> groceryListProductsModels){
        if(isNetworkAvailable()){
            mReference = mDatabase.getReference().child(GROCERYLISTSNODE);
            DatabaseReference pushRef = mReference.push();
            pushRef.setValue(groceryListsModel);
            String generated_GL_key = pushRef.getKey();

            Log.d("generated_GL_key", generated_GL_key);

            //Upis proizvoda za taj GL u firebase
            if(!isNullOrBlank(generated_GL_key)){
                for (GroceryListProductsModel product: groceryListProductsModels) {
                    DatabaseReference refProducts = mDatabase.getReference().child(GROCERYLISTPRODUCTSNODE).child(generated_GL_key).child(product.getProduct_key());
                    product.setProduct_key(null);
                    refProducts.setValue(product);
                }
                addGroceryListListener.groceryListAddedToDatabase(true, "Uspješno kreirano!");
            }
            else{
                addGroceryListListener.groceryListAddedToDatabase(true, "Greška prilikom upisa!");
            }
        }else
            showInternetMessageWarning();


    }

    private boolean isNullOrBlank(String s)
    {
        return (s == null || s.trim().equals(""));
    }

}
