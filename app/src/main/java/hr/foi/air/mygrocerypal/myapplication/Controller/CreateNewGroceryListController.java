package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.StoresListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;

public class CreateNewGroceryListController {

    private static final String STORESNODE  = "stores";
    private static final String GROCERYLISTSNODE  = "grocerylists";
    private static final String GROCERYLISTPRODUCTSNODE  = "grocerylistproducts";

    private StoresListener storesListener;
    private FirebaseDatabase firebaseDatabase;

    public CreateNewGroceryListController(Fragment fragment){
        storesListener = (StoresListener) fragment;
    }

    public void getAllStores(){

        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();
        Query query = firebaseDatabase.getReference().child(STORESNODE);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<StoresModel> stores = new ArrayList<>();
                for (DataSnapshot temp : dataSnapshot.getChildren()){
                    StoresModel storesModel = temp.getValue(StoresModel.class);
                    storesModel.setStore_id(temp.getKey());
                    stores.add(storesModel);
                }

                storesListener.storesReceived(stores);
                Log.d("getAllStores", "sizeStores" + Integer.toString(stores.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveGL_withProducts(GroceryListsModel groceryListsModel, List<GroceryListProductsModel> groceryListProductsModels){

        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference().child(GROCERYLISTSNODE);
        DatabaseReference pushRef = ref.push();
        pushRef.setValue(groceryListsModel);
        String generated_GL_key = pushRef.getKey();


        Log.d("generated_GL_key", generated_GL_key);
        String product_key = "";
        List<String> tempList = new ArrayList<>();
        DatabaseReference refproducts;

        if(!isNullOrBlank(generated_GL_key)){
            if (firebaseDatabase == null)
                firebaseDatabase = FirebaseDatabase.getInstance();
            for (GroceryListProductsModel product: groceryListProductsModels) {
                //product.setGrocery_list_key(generated_GL_key);
                product_key = product.getProduct_key();
                tempList.add(Integer.toString(product.getBought()));
                tempList.add(product.getName());
                tempList.add(Double.toString(product.getPrice()));
                tempList.add(Integer.toString(product.getQuantity()));
                DatabaseReference refProducts = firebaseDatabase.getReference().child(GROCERYLISTPRODUCTSNODE).child(generated_GL_key).child(product_key);
                refProducts.setValue(tempList);

                tempList.clear();
            }

            //Upis proizvoda za taj GL u firebase







        }
        else{
            //TO DO
            //javi fragmentu da nije upisano u bazu
        }



    }

    private boolean isNullOrBlank(String s)
    {
        return (s == null || s.trim().equals(""));
    }
    
    
}
