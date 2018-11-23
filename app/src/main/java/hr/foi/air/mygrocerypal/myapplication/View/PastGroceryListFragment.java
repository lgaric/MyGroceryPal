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

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class PastGroceryListFragment extends Fragment implements GroceryListListener {
    private GroceryListController pastGroceryListController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_past_grocerylist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //KREIRANJE KONTROLERA
        pastGroceryListController = new GroceryListController(this);
    }

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> groceryList) {

        if(groceryList != null){
            //DOHVATI RECYCLEVIEW
            RecyclerView recyclerView = getActivity().findViewById(R.id.recycler_view);

            //KORIŠTENJE LINEAR LAYOUT MANAGERA -> PIŠE DA TREBA U DOKUMENTACIJI
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            //NAPRAVI ADAPTER I POSTAVI GA
            //GroceryListAdapter groceryListAdapter = new GroceryListAdapter(groceryList);
            //recyclerView.setAdapter(groceryListAdapter);
        }
    }
}
