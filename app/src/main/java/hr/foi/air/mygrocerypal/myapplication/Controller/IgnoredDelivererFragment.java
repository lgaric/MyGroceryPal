package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Adapters.DelivererGLAdapter;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.DelivererIgnoredGroceryListController;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

    public class IgnoredDelivererFragment extends Fragment implements GroceryListListener, GroceryListOperationListener, GroceryListStatusListener{

    //vars
    DelivererIgnoredGroceryListController delivererIgnoredGroceryListController;
    ArrayList<GroceryListsModel> ignoredGroceryList;

    //widgets
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;


    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshRecyclerView();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer_ignored, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_ignored);
        recyclerView = view.findViewById(R.id.grocery_lists_ignored);

        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        if(delivererIgnoredGroceryListController == null){
            delivererIgnoredGroceryListController = new DelivererIgnoredGroceryListController(this, this);
            delivererIgnoredGroceryListController.loadAllIgnoredGroceryLists();
        }
        else
            delivererIgnoredGroceryListController.loadAllIgnoredGroceryLists();

        return view;
    }

    //PRIMA PREKO INTERFACE-A
    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList) {
        if(groceryList != null){
            ignoredGroceryList = groceryList;
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DelivererGLAdapter adapter = new DelivererGLAdapter(groceryList, this,1);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void buttonPressedOnGroceryList(GroceryListsModel groceryListsModel, GroceryListOperation operation) {
        switch(operation){
            case RETURN:
                delivererIgnoredGroceryListController.checkGroceryListStatus(groceryListsModel.getGrocerylist_key(), operation);
                break;
            case DETAILS:
                showGroceryListDetails(groceryListsModel);
                break;
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

    private void refreshRecyclerView(){
        if(delivererIgnoredGroceryListController != null)
            delivererIgnoredGroceryListController.loadAllIgnoredGroceryLists();
    }

    @Override
    public void groceryListStatusReceived(String groceryListID, String groceryListStatus, GroceryListOperation operation) {
        String message = "";
        if(groceryListStatus.equals("")) {
            Toast.makeText(getActivity(), "Pogreška prilikom dohvata Grocery List ID-a", Toast.LENGTH_SHORT).show();
            return;
        }else if(!GroceryListStatus.valueOf(groceryListStatus).equals(GroceryListStatus.CREATED)){
            Toast.makeText(getActivity(), "Kupovna lista je već prihvaćena", Toast.LENGTH_SHORT).show();
            CurrentUser.currentUser.getIgnoredLists().remove(groceryListID);
            return;
        }else if(operation == GroceryListOperation.RETURN)
            message = delivererIgnoredGroceryListController.returnGroceryListFromIgnored(groceryListID);
        else
            message = "Došlo je do greške!";

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            refreshRecyclerView();
        }
    }
