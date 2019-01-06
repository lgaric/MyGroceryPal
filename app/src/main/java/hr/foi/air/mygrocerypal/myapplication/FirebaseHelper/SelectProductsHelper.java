package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.SelectProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Model.CategoriesModel;
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

                    mSelectProductsListener.productsListByStoreReceived(productsList);
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

    public void loadProductCategories(){
        if(isNetworkAvailable()){
            mQuery = mDatabase.getReference().child(CATEGORIESNODE);
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<CategoriesModel> categories = new ArrayList<>();
                    CategoriesModel title = new CategoriesModel();
                    title.setCategory_id("1");
                    title.setName("Categories");
                    categories.add(title);
                    for (DataSnapshot temp : dataSnapshot.getChildren()){
                        CategoriesModel categoriesModel = temp.getValue(CategoriesModel.class);
                        categoriesModel.setCategory_id(temp.getKey());
                        categories.add(categoriesModel);
                    }

                    mSelectProductsListener.categoriesListReceived(categories);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }else
            showInternetMessageWarning();
    }

}
