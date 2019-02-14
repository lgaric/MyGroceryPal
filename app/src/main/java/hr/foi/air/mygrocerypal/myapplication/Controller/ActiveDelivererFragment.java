package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.GPSLocation;
import hr.foi.air.mygrocerypal.LocationListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.DelivererGLAdapter;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.DelivererGroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.SecondNavigationItem;

public class ActiveDelivererFragment extends Fragment implements LocationListener, GroceryListOperationListener,
        GroceryListStatusListener, SecondNavigationItem {
    private TextView mNoneActiveLists;

    SeekBar mSeekBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    TextView mRadius;
    Switch mGpsSwitch;

    GPSLocation mGpsLocation;

    DelivererGroceryListHelper mDelivererGroceryListHelper;

    ArrayList<GroceryListsModel> mAllActiveGroceryList;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshRecyclerView();
        }
    };

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(mRadius != null) {
                if(progress == 0)
                    mRadius.setText(Integer.toString(1));
                else
                    mRadius.setText(Integer.toString(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar mSeekBar) {
            // Do nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar mSeekBar) {
            // Do nothing
        }
    };

    private CompoundButton.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mGpsSwitch.isChecked())
                turnOnGps();
        }
    };

    /**
     * Inicijalizacija
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer_active, container, false);

        mSeekBar = view.findViewById(R.id.grocery_list_range);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mRecyclerView = view.findViewById(R.id.grocery_lists);
        mRadius = view.findViewById(R.id.radius);
        mGpsSwitch = view.findViewById(R.id.gps_switch);
        mNoneActiveLists = view.findViewById(R.id.noneDelivererActiveGL);

        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        mGpsSwitch.setOnClickListener(clickListener);

        if (mDelivererGroceryListHelper == null) {
            mDelivererGroceryListHelper = new DelivererGroceryListHelper(this);
            mDelivererGroceryListHelper.getAllActiveGroceryLists();
        }
        else
            mDelivererGroceryListHelper.getAllActiveGroceryLists();

        getContext().registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

        return view;
    }

    /**
     * Ukljucivanje GPS-a
     */
    public void turnOnGps(){
        if (mGpsLocation == null) {
            mGpsLocation = new GPSLocation(this);
            mGpsLocation.startLocationButtonClick();
        }
        else
            mGpsLocation.startLocationButtonClick();
    }

    /**
     * osvježi recycleview
     */
    private void refreshRecyclerView(){
        if(mDelivererGroceryListHelper != null)
            mDelivererGroceryListHelper.getAllActiveGroceryLists();
    }

    /**
     * Ako je dohvaćena lokacija od korisnika pomocu gps i ako je switch (R.id.gps_switch) ukljucen
     * koristi GPS lokaciju, inaće koristi adresu na kojoj se korisnik prijavio
     * @param mRadius
     */
    private void showFilteredList(int mRadius){
        if(mAllActiveGroceryList != null){
            ArrayList<GroceryListsModel> mFilteredList;
            if(CurrentUser.gpsLocation != null && mGpsSwitch.isChecked())
                mFilteredList = filterListUsingDistance(mRadius, CurrentUser.gpsLocation);
            else
                mFilteredList = filterListUsingDistance(mRadius, getLocation(CurrentUser.getCurrentUser.getLatitude(),
                        CurrentUser.getCurrentUser.getLongitude(), "USERLOCATION"));
            setRecyclerView(mFilteredList);
            setTextVisibility(mFilteredList);
        }else
            mNoneActiveLists.setVisibility(View.VISIBLE);
    }


    /**
     * Metoda na temelju latituda i longituda vraća objekt tipa Location
     * @param mLatitude
     * @param mLongitude
     * @param mLocationName
     * @return
     */
    private Location getLocation(double mLatitude, double mLongitude, String mLocationName){
        Location mLocation = new Location(mLocationName);
        mLocation.setLatitude(mLatitude);
        mLocation.setLongitude(mLongitude);
        return mLocation;
    }

    /**
     * Računanje udaljenost izmeđe svakog GL i lokacije korisnika
     * @param mRadius
     * @param mLocation
     * @return
     */
    private ArrayList<GroceryListsModel> filterListUsingDistance(int mRadius, Location mLocation){
        ArrayList<GroceryListsModel> mFilteredList = new ArrayList<>();
        for(int i = 0; i < mAllActiveGroceryList.size(); i++){
            Location groceryListLocation = getLocation(mAllActiveGroceryList.get(i).getLatitude(),
                    mAllActiveGroceryList.get(i).getLongitude(), "GROCERYLISTLOCATION");

            float distance = mLocation.distanceTo(groceryListLocation) / 1000;

            if(distance < mRadius)
                mFilteredList.add(mAllActiveGroceryList.get(i));
        }
        return mFilteredList;
    }

    /**
     * postavi adapter za recycleview
     * @param mGroceryList
     */
    private void setRecyclerView(ArrayList<GroceryListsModel> mGroceryList){
        if(mGroceryList != null) {
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DelivererGLAdapter adapter = new DelivererGLAdapter(mGroceryList, this,0);
            mRecyclerView.setAdapter(adapter);
        }
    }

    /**
     * Prikaži detalje groceryliste
     * @param mGroceryListsModel
     */
    private void showGroceryListDetails(GroceryListsModel mGroceryListsModel){
        Bundle bundle = new Bundle();
        bundle.putSerializable("GROCERY_LIST_MODEL", mGroceryListsModel);
        bundle.putBoolean("IS_DELIVERER", true);
        ShowGroceryListDetailsFragment fragment = new ShowGroceryListDetailsFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }


    /**
     * Metoda preko koje se dobivaju GL-ovi putem listenera
     * @param mGroceryList
     * @param mGroceryListStatus
     */
    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null){
            mAllActiveGroceryList = mGroceryList;
            showFilteredList(Integer.parseInt(mRadius.getText().toString().trim()));
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Postavljanje vrijednosti atributa mGpsLocation u CurrentUser klasi
     * @param mLocation
     */
    @Override
    public void locationReceived(Location mLocation) {
        if(mLocation != null) {
            Log.d("LOKACIJA", Double.toString(mLocation.getLatitude()) + " : " + Double.toString(mLocation.getLongitude()));
            CurrentUser.gpsLocation = mLocation;
        }
    }

    /**
     * Ako korisnik ne prihvati dozvolu za ukljucivanjem GPS, stavi switch na off
     * @param mErrorMessage
     */
    @Override
    public void dataNotReceived(String mErrorMessage) {
        if(mErrorMessage.equals("-"))
            mGpsSwitch.setChecked(false);
        else if(mErrorMessage.equals("+"))
            mGpsSwitch.setChecked(true);
        else
            Log.d("GPSERROR", mErrorMessage);
    }

    /**
     * Pritisak gumba na pojedini GL
     * @param mGroceryListsModel
     * @param mOperation
     */
    @Override
    public void buttonPressedOnGroceryList(GroceryListsModel mGroceryListsModel, GroceryListOperation mOperation) {
        switch (mOperation){
            case ACCEPT:
                mDelivererGroceryListHelper.checkGroceryListStatus(mGroceryListsModel.getGrocerylist_key(), mOperation);
                break;
            case IGNORE:
                mDelivererGroceryListHelper.checkGroceryListStatus(mGroceryListsModel.getGrocerylist_key(), mOperation);
                break;
            case DETAILS:
                showGroceryListDetails(mGroceryListsModel);
                break;
            default:
                break;
        }
    }


    /**
     * Dobivanje statusa pojedine grocery liste
     * @param mGroceryListID
     * @param mGroceryListStatus
     * @param mOperation
     */
    @Override
    public void groceryListStatusReceived(String mGroceryListID, String mGroceryListStatus, GroceryListOperation mOperation) {
        String message = "";
        if(mGroceryListStatus.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.getGroceryListError), Toast.LENGTH_SHORT).show();
            return;
        }
        if(mOperation == GroceryListOperation.ACCEPT)
            message = mDelivererGroceryListHelper.acceptGroceryList(mGroceryListID, mGroceryListStatus);
        else if (mOperation == GroceryListOperation.IGNORE)
            message = mDelivererGroceryListHelper.ignoreGroceryList(mGroceryListID);
        else
            message = getResources().getString(R.string.defaultErrorMessage);

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        refreshRecyclerView();
    }

    /**
     * Postavljanje vidljivosti grocery lista
     * @param mGroceryList
     */
    private void setTextVisibility(ArrayList<GroceryListsModel> mGroceryList){
        if(mGroceryList.size() > 0)
            mNoneActiveLists.setVisibility(View.GONE);
        else
            mNoneActiveLists.setVisibility(View.VISIBLE);
    }

    /**
     * Da li je korisnik prihvatio da se ukljuci GPS ?
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case GPSLocation.REQUEST_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                        dataNotReceived("+");
                        break;
                    case Activity.RESULT_CANCELED:
                        dataNotReceived("-");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(mGpsSwitchStateReceiver);
    }

    /**
     * Prati da li je korisnik rucno iskljucio GPS
     */
    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    dataNotReceived("-");
                    mGpsSwitch.setChecked(false);
                }
            }
        }
    };

    @Override
    public String getName(Context context) {
        return context.getResources().getString(R.string.activeCaps);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
