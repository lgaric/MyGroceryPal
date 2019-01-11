package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import hr.foi.air.mygrocerypal.myapplication.R;

public class FirebaseBaseHelper {
    //Firebase tablice
    protected static final String PRODUCTSNODE  = "products";
    protected static final String STORESNODE  = "stores";
    protected static final String GROCERYLISTSNODE  = "grocerylists";
    protected static final String CATEGORIESNODE = "categories";
    protected static final String GROCERYLISTPRODUCTSNODE  = "grocerylistproducts";
    protected static final String USERIGNOREDLISTNODE  = "userignoredlists";
    protected static final String GROCERYLISTSTATUSNODE = "status";
    protected static final String USERACCEPTEDIDNODE = "user_accepted_id";
    protected static final String USERNODE = "users";
    protected static final String USERIDNODE = "user_id";
    protected static final String ADDRESSNODE = "address";
    protected static final String TOWNNODE = "town";
    protected static final String LATITUDENODE = "latitude";
    protected static final String LONGITUDE = "longitude";
    protected static final String PHONENUMBERNODE = "phone_number";
    protected static final String BOUGHTNODE = "bought";


    //Firebase varijable
    protected FirebaseDatabase mDatabase;
    protected FirebaseAuth mAuth;
    protected DatabaseReference mReference;
    protected Query mQuery;
    protected Context mContext;

    public FirebaseBaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    protected Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected void showInternetMessageWarning(){
        if(mContext != null){
            Toast.makeText(mContext, mContext.getResources().getString(R.string.noInternetConnectionMessage), Toast.LENGTH_LONG).show();
        }
    }

}
