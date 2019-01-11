package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import hr.foi.air.mygrocerypal.myapplication.R;

public class UserInformationHelper extends FirebaseBaseHelper {

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
    public String updateUserAdress(String userID, String address, String town, Location location){
        if(isNetworkAvailable()){
            try{
                mDatabase.getReference().child(USERNODE).child(userID).child(ADDRESSNODE).setValue(address);
                mDatabase.getReference().child(USERNODE).child(userID).child(TOWNNODE).setValue(town);
                mDatabase.getReference().child(USERNODE).child(userID).child(LATITUDENODE).setValue(location.getLatitude());
                mDatabase.getReference().child(USERNODE).child(userID).child(LONGITUDE).setValue(location.getLongitude());
                return mContext.getResources().getString(R.string.addressChangeSuccess);
            }catch(Exception e) {
                Log.e(getClass().toString(), e.getMessage());
                return mContext.getResources().getString(R.string.addressChangeFail);
            }
        }else
            return mContext.getResources().getString(R.string.noInternetConnectionMessage);
    }

    /**
     * Postavi novi broj mobitela odabranog korisnika
     * @param userID
     * @param newPhoneNumber
     * @return
     */
    public String updateUserPhoneNumber(String userID, String newPhoneNumber){
        if(isNetworkAvailable()){
            try{
                mDatabase.getReference().child(USERNODE).child(userID).child(PHONENUMBERNODE).setValue(newPhoneNumber);
                return mContext.getResources().getString(R.string.phoneChangeSuccess);
            }catch(Exception e) {
                Log.e(getClass().toString(), e.getMessage());
                return mContext.getResources().getString(R.string.phoneChangeFail);
            }
        }else
            return mContext.getResources().getString(R.string.noInternetConnectionMessage);
    }
}
