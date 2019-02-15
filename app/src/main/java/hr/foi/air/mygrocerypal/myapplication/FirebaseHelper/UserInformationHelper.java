package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.myapplication.R;

public class UserInformationHelper extends FirebaseBaseHelper {

    /**
     * Konstruktor
     * @param context
     */
    public UserInformationHelper(Context context){
        this.mContext = context;
    }

    /**
     * Promijeni sve atribute vezane uz adresu na firebaseu
     * @param userID id korisnika
     * @param address adresa
     * @param town grad
     * @param location lokacija korisnika
     * @return
     */
    public boolean updateUserAdress(String userID, String address, String town, Location location){
        if(isNetworkAvailable()){
            try{
                mDatabase.getReference().child(USERNODE).child(userID).child(ADDRESSNODE).setValue(address);
                mDatabase.getReference().child(USERNODE).child(userID).child(TOWNNODE).setValue(town);
                mDatabase.getReference().child(USERNODE).child(userID).child(LATITUDENODE).setValue(location.getLatitude());
                mDatabase.getReference().child(USERNODE).child(userID).child(LONGITUDE).setValue(location.getLongitude());
                Toast.makeText(mContext, mContext.getString(R.string.addressChangeSuccess), Toast.LENGTH_LONG).show();
                return true;
            }catch(Exception e) {
                Log.e(getClass().toString(), e.getMessage());
                Toast.makeText(mContext, mContext.getString(R.string.addressChangeFail), Toast.LENGTH_LONG).show();
                return false;
            }
        }else{
            Toast.makeText(mContext, mContext.getString(R.string.noInternetConnectionMessage), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Postavi novi broj mobitela odabranog korisnika
     * @param userID
     * @param newPhoneNumber
     * @return
     */
    public boolean updateUserPhoneNumber(String userID, String newPhoneNumber){
        if(isNetworkAvailable()){
            try{
                mDatabase.getReference().child(USERNODE).child(userID).child(PHONENUMBERNODE).setValue(newPhoneNumber);
                Toast.makeText(mContext, mContext.getResources().getString(R.string.phoneChangeSuccess), Toast.LENGTH_LONG).show();
                return true;
            }catch(Exception e) {
                Log.e(getClass().toString(), e.getMessage());
                Toast.makeText(mContext, mContext.getResources().getString(R.string.phoneChangeFail), Toast.LENGTH_LONG).show();
                return false;
            }
        }else {
            Toast.makeText(mContext, mContext.getString(R.string.noInternetConnectionMessage), Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
