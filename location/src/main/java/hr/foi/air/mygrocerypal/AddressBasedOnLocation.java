package hr.foi.air.mygrocerypal;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressBasedOnLocation {

    public AddressBasedOnLocation(Activity activity, Fragment fragment) {
        this.locationListener = (LocationListener) fragment;
        this.activity = activity;
    }

    private LocationListener locationListener;
    private Activity activity;
    private final String TAG = "Geolociranje";
    private StringBuilder fullAddress;

    /**
     * Metoda za dobivanje tocne adrese u obliku stringa na temelju geolokacije
     * @param location
     */
    public String GetAddress(final Location location)
    {
        List<Address> addresses = null;
        fullAddress = new StringBuilder();
        Geocoder geocoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
        String errorMessage = "";
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException ioException) {
            errorMessage = "Problemi s mrezom / IO problemi";
            Log.e(TAG, errorMessage, ioException);
            return null;
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Nedozovljene vrijednosti geografske sirine i duzine";
            Log.e(TAG, errorMessage, illegalArgumentException);
            return null;
        }

        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Adresa na temelju parametara nije pronadena";
                Log.e(TAG, errorMessage);
                return null;
            }
        } else {
            Address address = addresses.get(0);

            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                fullAddress.append(address.getAddressLine(i)).append("\n");
            }
        }
        return fullAddress.toString();
    }
}
