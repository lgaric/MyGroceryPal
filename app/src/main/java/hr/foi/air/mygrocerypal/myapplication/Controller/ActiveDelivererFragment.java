package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.DelivererGroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ActiveDelivererFragment extends Fragment implements LocationListener, GroceryListOperationListener,
        GroceryListStatusListener {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer_active, container, false);

        mSeekBar = view.findViewById(R.id.grocery_list_range);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mRecyclerView = view.findViewById(R.id.grocery_lists);
        mRadius = view.findViewById(R.id.radius);
        mGpsSwitch = view.findViewById(R.id.gps_switch);

        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        mGpsSwitch.setOnClickListener(clickListener);

        if (mDelivererGroceryListHelper == null) {
            mDelivererGroceryListHelper = new DelivererGroceryListHelper(this);
            mDelivererGroceryListHelper.getAllActiveGroceryLists();
        }
        else
            mDelivererGroceryListHelper.getAllActiveGroceryLists();

        return view;
    }

    public void turnOnGps(){
        if (mGpsLocation == null) {
            mGpsLocation = new GPSLocation(getActivity(), this);
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
            ArrayList<GroceryListsModel> temporary;
            if(CurrentUser.gpsLocation != null && mGpsSwitch.isChecked())
                temporary = filterListUsingDistance(mRadius, CurrentUser.gpsLocation);
            else
                temporary = filterListUsingDistance(mRadius, getLocation(CurrentUser.getCurrentUser.getLatitude(),
                        CurrentUser.getCurrentUser.getLongitude(), "USERLOCATION"));
            setmRecyclerView(temporary);
        }
    }


    /**
     * Metoda na temelju latituda i longituda vraća objekt tipa Location
     * @param mLatitude
     * @param mLongitude
     * @param mLocationName
     * @return
     */
    private Location getLocation(double mLatitude, double mLongitude, String mLocationName){
        Location tempLocation = new Location(mLocationName);
        tempLocation.setLatitude(mLatitude);
        tempLocation.setLongitude(mLongitude);
        return tempLocation;
    }

    /**
     * Računanje udaljenost izmeđe svakog GL i lokacije korisnika
     * @param mRadius
     * @param mLocation
     * @return
     */
    private ArrayList<GroceryListsModel> filterListUsingDistance(int mRadius, Location mLocation){
        ArrayList<GroceryListsModel> temporary = new ArrayList<>();
        for(int i = 0; i < mAllActiveGroceryList.size(); i++){
            Location groceryListLocation = getLocation(mAllActiveGroceryList.get(i).getLatitude(),
                    mAllActiveGroceryList.get(i).getLongitude(), "GROCERYLISTLOCATION");

            float distance = mLocation.distanceTo(groceryListLocation) / 1000;

            if(distance < mRadius)
                temporary.add(mAllActiveGroceryList.get(i));
        }
        return temporary;
    }

    /**
     * postavi adapter za recycleview
     * @param mGroceryList
     */
    private void setmRecyclerView(ArrayList<GroceryListsModel> mGroceryList){
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
        ShowGroceryListDetailsFragment fragment = new ShowGroceryListDetailsFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    //IMPLEMENTACIJA INTERFEJSA

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList) {
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
        Log.d("GPSERROR", mErrorMessage);
        if(mGpsSwitch.isChecked())
            mGpsSwitch.setChecked(false);
    }

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


    @Override
    public void groceryListStatusReceived(String mGroceryListID, String mGroceryListStatus, GroceryListOperation mOperation) {
        String message = "";
        if(mGroceryListStatus.equals("")) {
            Toast.makeText(getActivity(), "Pogreška prilikom dohvata Grocery List ID-a", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mOperation == GroceryListOperation.ACCEPT)
            message = mDelivererGroceryListHelper.acceptGroceryList(mGroceryListID, mGroceryListStatus);
        else if (mOperation == GroceryListOperation.IGNORE)
            message = mDelivererGroceryListHelper.ignoreGroceryList(mGroceryListID);
        else
            message = "Došlo je do greške!";

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        refreshRecyclerView();
    }
}
