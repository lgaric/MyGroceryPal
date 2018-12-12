package hr.foi.air.mygrocerypal.myapplication.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.DelivererIgnoredGroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class IgnoredDelivererFragment extends Fragment implements GroceryListListener {

    //vars
    DelivererIgnoredGroceryListController delivererIgnoredGroceryListController;

    //widgets
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer_ignored, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_ignored);
        recyclerView = view.findViewById(R.id.grocery_lists_ignored);
        delivererIgnoredGroceryListController = new DelivererIgnoredGroceryListController(this);

        return view;
    }

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList) {

    }
}
