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
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.AddGroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;

public class CreateNewGroceryListController {

    private static final String STORESNODE  = "stores";
    private static final String GROCERYLISTSNODE  = "grocerylists";
    private static final String GROCERYLISTPRODUCTSNODE  = "grocerylistproducts";
    private static final String USERSNODE  = "users";


    private AddGroceryListListener addGroceryListListener;
    private FirebaseDatabase firebaseDatabase;

    public CreateNewGroceryListController(Fragment fragment){
        addGroceryListListener = (AddGroceryListListener) fragment;
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

                addGroceryListListener.storesReceived(stores);
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

        //Upis proizvoda za taj GL u firebase
        if(!isNullOrBlank(generated_GL_key)){
            if (firebaseDatabase == null)
                firebaseDatabase = FirebaseDatabase.getInstance();
            for (GroceryListProductsModel product: groceryListProductsModels) {
                DatabaseReference refProducts = firebaseDatabase.getReference().child(GROCERYLISTPRODUCTSNODE).child(generated_GL_key).child(product.getProduct_key());
                product.setProduct_key(null);
                refProducts.setValue(product);
            }
            addGroceryListListener.groceryListAddedToDatabase(true, "Uspješno kreirano!");

        }
        else{
            addGroceryListListener.groceryListAddedToDatabase(true, "Greška prilikom upisa!");
        }



    }

    private boolean isNullOrBlank(String s)
    {
        return (s == null || s.trim().equals(""));
    }

}
