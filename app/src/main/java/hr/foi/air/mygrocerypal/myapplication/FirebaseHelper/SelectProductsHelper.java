package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.SelectProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;

public class SelectProductsHelper extends FirebaseBaseHelper{
    private SelectProductsListener mSelectProductsListener;

    public SelectProductsHelper(SelectProductsListener mSelectProductsListener){
        this.mContext = ((Fragment) mSelectProductsListener).getContext();
        this.mSelectProductsListener = mSelectProductsListener;
    }

    /**
     * Dohvati sve proizvode po trgovini
     * @param mStoreName
     */
    public void loadProductsByStore(String mStoreName) {
        if (mStoreName == null)
            return;

        if(isNetworkAvailable()) {
            mQuery = mDatabase.getReference().child(PRODUCTSNODE).orderByChild("store_name").equalTo(mStoreName);

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<ProductsModel> productsList = new ArrayList<>();

                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        ProductsModel product = temp.getValue(ProductsModel.class);
                        product.setProduct_key(temp.getKey());
                        productsList.add(product);
                    }

                    mSelectProductsListener.productsListReceived(productsList);
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
