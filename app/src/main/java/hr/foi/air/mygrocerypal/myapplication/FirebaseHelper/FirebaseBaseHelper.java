package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.regex.Pattern;

public class FirebaseBaseHelper {
    //Firebase tablice
    protected static final String PRODUCTSNODE  = "products";
    protected static final String STORESNODE  = "stores";
    protected static final String GROCERYLISTSNODE  = "grocerylists";
    protected static final String GROCERYLISTPRODUCTSNODE  = "grocerylistproducts";
    protected static final String USERIGNOREDLISTNODE  = "userignoredlists";
    protected static final String GROCERYLISTSTATUS = "status";
    protected static final String USERACCEPTEDID = "user_accepted_id";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");


    //Firebase varijable
    protected FirebaseDatabase mDatabase;
    protected FirebaseAuth mAuth;
    protected DatabaseReference mReference;
    protected Query mQuery;

    public FirebaseBaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean validateEmail(String emailTxt){
        String email = emailTxt;
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else return true;
    }

    public boolean validatePassword(String passwordTxt){
        String password = passwordTxt;
        if (!PASSWORD_PATTERN.matcher(password).matches()){
            return false;
        }else return true;
    }

    public boolean validateRetypedPassword(String passwordTxt, String retypedPasswordTxt){
        String firstPassword = passwordTxt;
        String secondPassword = retypedPasswordTxt;
        if(!firstPassword.equals(secondPassword)){
            return false;
        }else return true;
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
