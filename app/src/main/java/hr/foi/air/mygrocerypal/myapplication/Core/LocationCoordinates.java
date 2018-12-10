package hr.foi.air.mygrocerypal.myapplication.Core;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.AddressBasedOnLocation;
import hr.foi.air.mygrocerypal.GPSLocation;
import hr.foi.air.mygrocerypal.LocationListener;
import hr.foi.air.mygrocerypal.myapplication.BuildConfig;
import hr.foi.air.mygrocerypal.myapplication.R;


public class LocationCoordinates extends Fragment {

    private Button btnStartUpdates;
    private TextView txtLocationResult;
    private TextView txtFullAddress;
    private EditText txtAdresa;
    private Button ispisLokacijaBtn;
    private GPSLocation currentLocationInstance;
    private AddressBasedOnLocation getAddress;
    private Location mCurrentLocation;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_start_location_updates:
                    getDeviceLocation();
                    break;
                case R.id.btnIspisLokacije:
                    getLocationBasedOnAddress();
                    break;
            }
        }
    };

    // metoda za pokretanje dohvata adrese na temelju dobivene adrese geolociranjem - napravljeno za testiranje

    private void getLocationBasedOnAddress() {
        if(mCurrentLocation == null) {
            Toast.makeText(getContext(), "Jos nije dobivena lokacija", Toast.LENGTH_SHORT).show();
            return;
        }
        if(getAddress == null)
            getAddress = new AddressBasedOnLocation(getActivity(), this);
        txtFullAddress.setText(getAddress.GetAddress(mCurrentLocation));
    }

    // metoda za pokretanje geolociranja uredaja

    private void getDeviceLocation() {
        if(currentLocationInstance == null) {
            GPSLocation currentLocation = new GPSLocation(getActivity(), this);
            currentLocation.startLocationButtonClick();
        } else {
            currentLocationInstance.startLocationButtonClick();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnStartUpdates = view.findViewById(R.id.btn_start_location_updates);
        btnStartUpdates.setOnClickListener(clickListener);
        ispisLokacijaBtn = view.findViewById(R.id.btnIspisLokacije);
        ispisLokacijaBtn.setOnClickListener(clickListener);

        txtLocationResult = view.findViewById(R.id.location_result);
        txtFullAddress = view.findViewById(R.id.full_address);
        txtAdresa = view.findViewById(R.id.txtAdresa);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    // potrebna metoda da se otvore settingsi ako korisnik odbije dati GPS - uspjesno se izvodi

    /**
     * Metoda kojom se otvaraju postavke za omogucivanje lokacije za aplikaciju
     */
    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Metode iz interfacea u kodu ispod

    /*
    @Override
    public Location locationReceived(Location location) {
        if (location != null) {
            mCurrentLocation = location;
            Location userLocation = new Location("");
            userLocation.setLongitude(CurrentUser.currentUser.getLongitude());
            userLocation.setLatitude(CurrentUser.currentUser.getLatitude());
            final float udaljenostMetri = location.distanceTo(userLocation);

            return mCurrentLocation;
        }
        return null;
    }

    @Override
    public String dataNotReceived(String errorMessage) {
        if(errorMessage == "openSettings")
        {
            openSettings();
        }
        return errorMessage;
    }

    */
}
