package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryChangeStatusHelper extends FirebaseBaseHelper{

    public GroceryChangeStatusHelper(Context context){
        this.mContext = context;
    }

    /**
     * Zavrsi kupovinu GL-a
     * @param mModel
     * @return
     */
    public Boolean setStatusToFinished(GroceryListsModel mModel){
        if(isNetworkAvailable()){
            try {
                mDatabase.getReference().child(GROCERYLISTSNODE).child(mModel.getGrocerylist_key())
                        .child(GROCERYLISTSTATUSNODE).setValue(GroceryListStatus.FINISHED);

                for(GroceryListProductsModel productsModel : mModel.getProductsModels()){
                    mDatabase.getReference().child(GROCERYLISTPRODUCTSNODE).child(mModel.getGrocerylist_key())
                            .child(productsModel.getGrocery_list_key())
                            .child(BOUGHTNODE).setValue(productsModel.getBought());
                }

                return true;
            }
            catch (Exception e){
                Toast.makeText(mContext, mContext.getResources().getString(R.string.defaultErrorMessage), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.noInternetConnectionMessage), Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
