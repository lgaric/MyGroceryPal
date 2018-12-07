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
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.GroceryListAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.DelivererActiveGroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.ClickListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ActiveDelivererFragment extends Fragment implements GroceryListListener, ClickListener {

    SeekBar seekBar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView radius;

    DelivererActiveGroceryListController controller;

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

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        controller = new DelivererActiveGroceryListController(this);
        controller.loadAllActiveGroceryLists();

        return view;
    }

    private void refreshRecyclerView(){
        if(controller != null)
            controller.loadAllActiveGroceryLists();
    }

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList) {
        if(groceryList != null){
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            GroceryListAdapter adapter = new GroceryListAdapter(groceryList,this);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onItemSelect(GroceryListsModel groceryListsModel) {

    }
}
