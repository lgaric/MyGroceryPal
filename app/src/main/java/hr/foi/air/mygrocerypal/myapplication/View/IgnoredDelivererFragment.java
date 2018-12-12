package hr.foi.air.mygrocerypal.myapplication.View;

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

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.DelivererGLAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.DelivererIgnoredGroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

    public class IgnoredDelivererFragment extends Fragment implements GroceryListListener, GroceryListOperationListener {

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
            delivererIgnoredGroceryListController = new DelivererIgnoredGroceryListController(this);
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
}
