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

    SeekBar seekBar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView radius;
    Switch gpsSwitch;

    GPSLocation gpsLocation;

    DelivererGroceryListHelper controller;

    ArrayList<GroceryListsModel> allActiveGroceryList;

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshRecyclerView();
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(radius != null) {
                if(progress == 0)
                    radius.setText(Integer.toString(1));
                else
                    radius.setText(Integer.toString(progress));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }
    };

    private CompoundButton.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(gpsSwitch.isChecked())
                turnOnGps();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer_active, container, false);

        seekBar = view.findViewById(R.id.grocery_list_range);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.grocery_lists);
        radius = view.findViewById(R.id.radius);
        gpsSwitch = view.findViewById(R.id.gps_switch);

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        gpsSwitch.setOnClickListener(clickListener);

        if (controller == null) {
            controller = new DelivererGroceryListHelper(this);
            controller.getAllActiveGroceryLists();
        }
        else
            controller.getAllActiveGroceryLists();

        return view;
    }

    public void turnOnGps(){
        if (gpsLocation == null) {
            gpsLocation = new GPSLocation(getActivity(), this);
            gpsLocation.startLocationButtonClick();
        }
        else
            gpsLocation.startLocationButtonClick();
    }

    /**
     * osvježi recycleview
     */
    private void refreshRecyclerView(){
        if(controller != null)
            controller.getAllActiveGroceryLists();
    }

    /**
     * Ako je dohvaćena lokacija od korisnika pomocu gps i ako je switch (R.id.gps_switch) ukljucen
     * koristi GPS lokaciju, inaće koristi adresu na kojoj se korisnik prijavio
     * @param radius
     */
    private void showFilteredList(int radius){
        if(allActiveGroceryList != null){
            ArrayList<GroceryListsModel> temporary;
            if(CurrentUser.gpsLocation != null && gpsSwitch.isChecked())
                temporary = filterListUsingDistance(radius, CurrentUser.gpsLocation);
            else
                temporary = filterListUsingDistance(radius, getLocation(CurrentUser.getCurrentUser.getLatitude(),
                        CurrentUser.getCurrentUser.getLongitude(), "USERLOCATION"));
            setRecyclerView(temporary);
        }
    }


    /**
     * Metoda na temelju latituda i longituda vraća objekt tipa Location
     * @param latitude
     * @param longitude
     * @param locationName
     * @return
     */
    private Location getLocation(double latitude,double longitude, String locationName){
        Location tempLocation = new Location(locationName);
        tempLocation.setLatitude(latitude);
        tempLocation.setLongitude(longitude);
        return tempLocation;
    }

    /**
     * Računanje udaljenost izmeđe svakog GL i lokacije korisnika
     * @param radius
     * @param location
     * @return
     */
    private ArrayList<GroceryListsModel> filterListUsingDistance(int radius, Location location){
        ArrayList<GroceryListsModel> temporary = new ArrayList<>();
        for(int i = 0; i < allActiveGroceryList.size(); i++){
            Location groceryListLocation = getLocation(allActiveGroceryList.get(i).getLatitude(),
                    allActiveGroceryList.get(i).getLongitude(), "GROCERYLISTLOCATION");

            float distance = location.distanceTo(groceryListLocation) / 1000;

            if(distance < radius)
                temporary.add(allActiveGroceryList.get(i));
        }
        return temporary;
    }

    /**
     * postavi adapter za recycleview
     * @param groceryList
     */
    private void setRecyclerView(ArrayList<GroceryListsModel> groceryList){
        if(groceryList != null) {
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DelivererGLAdapter adapter = new DelivererGLAdapter(groceryList, this,0);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Prikaži detalje groceryliste
     * @param groceryListsModel
     */
    private void showGroceryListDetails(GroceryListsModel groceryListsModel){
        Bundle bundle = new Bundle();
        bundle.putSerializable("GROCERY_LIST_MODEL", groceryListsModel);
        ShowGroceryListDetailsFragment fragment = new ShowGroceryListDetailsFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    //IMPLEMENTACIJA INTERFEJSA

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList) {
        if(groceryList != null){
            allActiveGroceryList = groceryList;
            showFilteredList(Integer.parseInt(radius.getText().toString().trim()));
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Postavljanje vrijednosti atributa gpsLocation u CurrentUser klasi
     * @param location
     */
    @Override
    public void locationReceived(Location location) {
        if(location != null) {
            Log.d("LOKACIJA", Double.toString(location.getLatitude()) + " : " + Double.toString(location.getLongitude()));
            CurrentUser.gpsLocation = location;
        }
    }

    /**
     * Ako korisnik ne prihvati dozvolu za ukljucivanjem GPS, stavi switch na off
     * @param errorMessage
     */
    @Override
    public void dataNotReceived(String errorMessage) {
        Log.d("GPSERROR", errorMessage);
        if(gpsSwitch.isChecked())
            gpsSwitch.setChecked(false);
    }

    @Override
    public void buttonPressedOnGroceryList(GroceryListsModel groceryListsModel, GroceryListOperation operation) {
        switch (operation){
            case ACCEPT:
                controller.checkGroceryListStatus(groceryListsModel.getGrocerylist_key(), operation);
                break;
            case IGNORE:
                controller.checkGroceryListStatus(groceryListsModel.getGrocerylist_key(), operation);
                break;
            case DETAILS:
                showGroceryListDetails(groceryListsModel);
                break;
            default:
                break;
        }
    }


    @Override
    public void groceryListStatusReceived(String groceryListID, String groceryListStatus, GroceryListOperation operation) {
        String message = "";
        if(groceryListStatus.equals("")) {
            Toast.makeText(getActivity(), "Pogreška prilikom dohvata Grocery List ID-a", Toast.LENGTH_SHORT).show();
            return;
        }
        if(operation == GroceryListOperation.ACCEPT)
            message = controller.acceptGroceryList(groceryListID, groceryListStatus);
        else if (operation == GroceryListOperation.IGNORE)
            message = controller.ignoreGroceryList(groceryListID);
        else
            message = "Došlo je do greške!";

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        refreshRecyclerView();
    }
}