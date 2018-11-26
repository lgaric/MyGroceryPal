package hr.foi.air.mygrocerypal.myapplication.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.ClickListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.GroceryListAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ShowGroceryListFragment extends Fragment implements GroceryListListener, ClickListener {
    private GroceryListController pastGroceryListController;
    private boolean active;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "FLOATINGBUTTON", Toast.LENGTH_LONG).show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        active = getArguments().getBoolean("ACTIVE");

        if(!active)
            return inflater.inflate(R.layout.fragment_client_past_grocerylist, container, false);
        else
            return inflater.inflate(R.layout.fragment_client_active_grocerylist, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pastGroceryListController = new GroceryListController(this);

        if(!active)
            pastGroceryListController.loadGroceryLists(GroceryListStatus.FINISHED);
        else {
            getView().findViewById(R.id.floatingButtonAdd).setOnClickListener(onClickListener);
            pastGroceryListController.loadGroceryLists(GroceryListStatus.ACCEPTED);
        }

    }

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList) {

        if(groceryList != null){
            RecyclerView recyclerView = getActivity().findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            GroceryListAdapter groceryListAdapter = new GroceryListAdapter(groceryList, this);
            recyclerView.setAdapter(groceryListAdapter);
        }
    }

    @Override
    public void onItemSelect(GroceryListsModel groceryListsModel) {
        ClientGroceryListFragment clientGroceryListFragment = (ClientGroceryListFragment)this.getParentFragment();
        clientGroceryListFragment.showGroceryListDetails(groceryListsModel);
    }

}
