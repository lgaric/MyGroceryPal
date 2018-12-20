package hr.foi.air.mygrocerypal.myapplication.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Adapters.GroceryListAdapter;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.GroceryListController;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.ClickListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ClientGroceryListFragment extends Fragment implements View.OnClickListener, GroceryListListener, ClickListener {

    private GroceryListController pastGroceryListController;
    private Button activeGrocerylistBtn, pastGroceryListBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingButtonAdd;
    private RecyclerView recyclerView;
    private GroceryListAdapter groceryListAdapter;
    /*
    0 -> AKTUALNI GROCERYLISTS
    1 -> PROŠLI GROCERYLISTS
     */
    boolean active;

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            showGroceryLists();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_grocerylist, container, false);

        activeGrocerylistBtn = view.findViewById(R.id.active_client_btn);
        pastGroceryListBtn = view.findViewById(R.id.past_client_btn);
        swipeRefreshLayout = view.findViewById(R.id.swiperefreshPastLists);
        floatingButtonAdd = view.findViewById(R.id.floatingButtonAdd);
        recyclerView = view.findViewById(R.id.recycler_view);

        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        activeGrocerylistBtn.setOnClickListener(this);
        pastGroceryListBtn.setOnClickListener(this);
        floatingButtonAdd.setOnClickListener(this);

        return view;
    }

    private void showGroceryLists(){
        if(active)
            pastGroceryListController.loadGroceryLists(GroceryListStatus.ACCEPTED);
        else
            pastGroceryListController.loadGroceryLists(GroceryListStatus.FINISHED);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        pastGroceryListController = new GroceryListController(this);
        super.onViewCreated(view, savedInstanceState);
        active = true;
        loadGroceryListToRecyclerView(GroceryListStatus.ACCEPTED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.active_client_btn:
                //AKO NISU PRIKAZANI AKTIVNI GL ONDA IH PRIKAZI
                if(!active) {
                    loadGroceryListToRecyclerView(GroceryListStatus.ACCEPTED);
                    active = true;
                }
                break;
            case R.id.past_client_btn:
                if(active){
                    loadGroceryListToRecyclerView(GroceryListStatus.FINISHED);
                    active = false;
                }
                break;
            case R.id.floatingButtonAdd:
                CreateNewGroceryListFragment createNewGroceryListFragment = new CreateNewGroceryListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, createNewGroceryListFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    private void loadGroceryListToRecyclerView(GroceryListStatus status){
        pastGroceryListController.loadGroceryLists(status);
    }


    //NEZANIMA NAS
    private void loadFragmentDetails(GroceryListsModel groceryListsModel){
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
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            groceryListAdapter = new GroceryListAdapter(groceryList, this);
            recyclerView.setAdapter(groceryListAdapter);

            //MAKNI OZNAKU ZA OSVJEZAVANJE
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemSelect(GroceryListsModel groceryListsModel) {
        loadFragmentDetails(groceryListsModel);
    }
}
