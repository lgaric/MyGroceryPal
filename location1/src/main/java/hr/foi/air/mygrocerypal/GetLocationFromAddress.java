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

public class GetLocationFromAddress {

    private LocationListener locationListener;

    public GetLocationFromAddress(Fragment fragment) {
        locationListener = (LocationListener) fragment;
    }

    /**
     * Metoda za dobivanje geografske sirine i duzine na temelju proslijedene adrese
     * @param address
     * @param context
     */
    public void getLocationBasedOnAddress(String address, Context context)
    {
        String errorMessage = "";
        if(address.isEmpty())
        {
            errorMessage = "Adresa nije unesena";
            setErrorMessage(errorMessage);
            return;
        }

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if(addresses.size() > 0)
            {
                Location location = new Location("");

                location.setLatitude(addresses.get(0).getLatitude());
                location.setLongitude(addresses.get(0).getLongitude());

                locationListener.locationReceived(location);
            }
        }
        catch(Exception e){
            errorMessage = "Nije moguce pronaci adresu";
            Log.e("Lokacija", errorMessage);

            setErrorMessage(errorMessage);
        }
    }

    private void setErrorMessage(String errorMessage)
    {
        locationListener.locationNotReceived(errorMessage);
    }
}
