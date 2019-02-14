package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
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
import hr.foi.air.mygrocerypal.myapplication.R;

public class CreateNewGroceryListHelper extends FirebaseBaseHelper{
    private AddGroceryListListener mAddGroceryListListener;

    /**
     * Konstruktor
     * @param mAddGroceryListListener
     */
    public CreateNewGroceryListHelper(AddGroceryListListener mAddGroceryListListener){
        if(mAddGroceryListListener instanceof Fragment)
            this.mContext = ((Fragment)mAddGroceryListListener).getContext();
        else
            this.mContext = (Context)mAddGroceryListListener;

        this.mAddGroceryListListener = mAddGroceryListListener;
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
                    if(mAddGroceryListListener != null)
                        mAddGroceryListListener.storesReceived(stores);
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
     * @param mGroceryListsModel
     * @param mGroceryListProductsModels
     */
    public void saveGroceryListWithProducts(GroceryListsModel mGroceryListsModel, List<GroceryListProductsModel> mGroceryListProductsModels){
        if(isNetworkAvailable()){
            mReference = mDatabase.getReference().child(GROCERYLISTSNODE);
            DatabaseReference pushRef = mReference.push();
            pushRef.setValue(mGroceryListsModel);
            String generated_GL_key = pushRef.getKey();

            //Upis proizvoda za taj GL u firebase
            if(!isNullOrBlank(generated_GL_key)){
                for (GroceryListProductsModel product: mGroceryListProductsModels) {
                    DatabaseReference refProducts = mDatabase.getReference().child(GROCERYLISTPRODUCTSNODE).child(generated_GL_key).child(product.getProduct_key());
                    product.setProduct_key(null);
                    refProducts.setValue(product);
                }
                if(mAddGroceryListListener != null)
                    mAddGroceryListListener.groceryListAddedToDatabase(true, mContext.getResources().getString(R.string.saveSuccess));
            }
            else{
                if(mAddGroceryListListener != null)
                    mAddGroceryListListener.groceryListAddedToDatabase(true, mContext.getResources().getString(R.string.saveFail));
            }
        }else
            showInternetMessageWarning();


    }

    /**
     * Provjeravanje ispravnosti
     * @param s
     * @return
     */
    private boolean isNullOrBlank(String s)
    {
        return (s == null || s.trim().equals(""));
    }

}
