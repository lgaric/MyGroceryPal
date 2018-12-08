package hr.foi.air.mygrocerypal;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class LocationBasedOnAddress {

    private LocationListener locationListener;

    public LocationBasedOnAddress(Fragment fragment) {
        locationListener = (LocationListener) fragment;
    }

    /**
     * Metoda za dobivanje geografske sirine i duzine na temelju proslijedene adrese
     * @param address
     * @param context
     */
    public Location GetLocation(String address, Context context) {
        String errorMessage = "";
        if(address.isEmpty()) {
            return null;
        }

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if(addresses.size() > 0) {

                Location receivedLocation = new Location("");
                receivedLocation.setLatitude(addresses.get(0).getLatitude());
                receivedLocation.setLongitude(addresses.get(0).getLongitude());
                return receivedLocation;
            }
        }
        catch(Exception e){
            errorMessage = "Nije moguce pronaci adresu";
            Log.e("Lokacija", errorMessage);
        }
        return null;
    }
}
