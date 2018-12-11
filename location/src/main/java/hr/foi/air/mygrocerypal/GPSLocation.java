package hr.foi.air.mygrocerypal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GPSLocation {

    private static final String TAG = "Geolocating";
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 120000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 120000;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Activity mCurrentActivity;
    private Boolean mRequestingLocationUpdates;
    private LocationListener locationListener;
    private String errorMessage;

    public GPSLocation(Activity activity, LocationListener locationListener) {
        this.mCurrentActivity = activity;
        this.locationListener = locationListener;
        init();
    }

    /**
     * Metoda inicijaliziranja potrebnih API-ja za geolociranje
     */
    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mCurrentActivity);
        mSettingsClient = LocationServices.getSettingsClient(mCurrentActivity);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                locationListener.locationReceived(mCurrentLocation);
            }
        };

        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        errorMessage = "";
    }

    /**
     * Metoda kojom geolociramo uredaj
     */
    public void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(mCurrentActivity, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "Svi zahtjevi za dohvat lokacije su zadovoljeni.");
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        //locationListener.locationReceived(mCurrentLocation);
                    }
                })
                .addOnFailureListener(mCurrentActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    errorMessage = "Nisu ispunjeni lokacijski zahtjevi aplikacije";
                                    Log.i(TAG, errorMessage);
                                    try {
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(mCurrentActivity, REQUEST_CHECK_SETTINGS);
                                    } catch (IntentSender.SendIntentException sie) {
                                        errorMessage = "Ne ispunjavanje zahtjeva";
                                        Log.i(TAG, errorMessage + sie);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    errorMessage = "Potrebno je namjestiti lokacijske postavke aplikacije";
                                    Log.e(TAG, errorMessage);

                                    Toast.makeText(mCurrentActivity, errorMessage, Toast.LENGTH_LONG).show();
                            }
                            setErrorMessage(errorMessage);

                    }
                });
    }

    /**
     * Metoda koja pokrece postupak geolociranja pritiskom na gumb
     */
    public void startLocationButtonClick() {
        Dexter.withActivity(mCurrentActivity)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            errorMessage = "openSettings";
                            setErrorMessage(errorMessage);
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void setErrorMessage(String errorMessage)
    {
        locationListener.dataNotReceived(errorMessage);
    }
}
