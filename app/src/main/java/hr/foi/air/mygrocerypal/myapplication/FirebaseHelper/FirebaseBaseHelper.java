package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FirebaseBaseHelper {
    //Firebase tablice
    protected static final String STORESNODE  = "stores";
    protected static final String GROCERYLISTSNODE  = "grocerylists";
    protected static final String GROCERYLISTPRODUCTSNODE  = "grocerylistproducts";
    protected static final String USERIGNOREDLISTNODE  = "userignoredlists";
    protected static final String GROCERYLISTSTATUS = "status";
    protected static final String USERACCEPTEDID = "user_accepted_id";
    protected static final String USERNODE = "users";


    //Firebase varijable
    protected FirebaseDatabase mDatabase;
    protected FirebaseAuth mAuth;
    protected DatabaseReference mReference;
    protected Query mQuery;

    public FirebaseBaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    /*
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    */
}
