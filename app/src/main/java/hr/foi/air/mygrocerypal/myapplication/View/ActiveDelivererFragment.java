package hr.foi.air.mygrocerypal.myapplication.View;

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
import java.util.List;

import hr.foi.air.mygrocerypal.GPSLocation;
import hr.foi.air.mygrocerypal.LocationListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.DelivererGLAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.GroceryListAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.DelivererActiveGroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListProductsController;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListUserController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.ClickListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ActiveDelivererFragment extends Fragment implements GroceryListListener, LocationListener, GroceryListOperationListener{

    SeekBar seekBar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView radius;
    Switch gpsSwitch;

    GPSLocation gpsLocation;

    DelivererActiveGroceryListController controller;

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

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

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

        controller = new DelivererActiveGroceryListController(this);
        controller.loadAllActiveGroceryLists();

        if(gpsLocation == null) {
            gpsLocation = new GPSLocation(getActivity(), this);
            gpsLocation.startLocationUpdates();
        }

        return view;
    }

    private void refreshRecyclerView(){

        Log.d("refreshRecyclerView", Integer.toString(android.os.Process.getThreadPriority(android.os.Process.myTid())));

        if(controller != null)
            controller.loadAllActiveGroceryLists();
    }

    /**
     * Ako je dohvaćena lokacija od korisnika pomocu gps i ako je switch (R.id.gps_switch) ukljucen
     * koristi GPS lokaciju, inaće koristi adresu na kojoj se korisnik prijavio
     * @param radius
     */
    private void showFilteredList(int radius){
        if(allActiveGroceryList != null){
            ArrayList<GroceryListsModel> temporery;
            if(CurrentUser.gpsLocation != null && gpsSwitch.isChecked())
                temporery = filterListUsingDistance(radius, CurrentUser.gpsLocation);
            else
                temporery = filterListUsingDistance(radius, getLocation(CurrentUser.currentUser.getLatitude(),
                        CurrentUser.currentUser.getLongitude(), "USERLOCATION"));
            setRecyclerView(temporery);
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
        ArrayList<GroceryListsModel> temporery = new ArrayList<>();
        for(int i = 0; i < allActiveGroceryList.size(); i++){
            Location groceryListLocation = getLocation(allActiveGroceryList.get(i).getLatitude(),
                    allActiveGroceryList.get(i).getLongitude(), "GROCERYLISTLOCATION");

            float distance = location.distanceTo(groceryListLocation) / 1000;

            if(distance < radius)
                temporery.add(allActiveGroceryList.get(i));
        }
        return temporery;
    }

    private void setRecyclerView(ArrayList<GroceryListsModel> groceryList){
        if(groceryList != null) {
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DelivererGLAdapter adapter = new DelivererGLAdapter(groceryList, this,0);
            recyclerView.setAdapter(adapter);
        }
    }

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

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList) {
        if(groceryList != null){
            allActiveGroceryList = groceryList;
            showFilteredList(Integer.parseInt(radius.getText().toString().trim()));
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void locationReceived(Location location) {
        if(location != null) {
            Log.d("LOKACIJA", Double.toString(location.getLatitude()) + " : " + Double.toString(location.getLongitude()));
            CurrentUser.gpsLocation = location;
        }
    }

    @Override
    public void dataNotReceived(String errorMessage) {
        Log.d("GPSLOCATION", errorMessage);
    }

    @Override
    public void buttonPressedOnGroceryList(GroceryListsModel groceryListsModel, GroceryListOperation operation) {
        switch (operation){
            case ACCEPT:
                break;
            case IGNORE:
                break;
            case DETAILS:
                showGroceryListDetails(groceryListsModel);
                break;
        }
    }
}
