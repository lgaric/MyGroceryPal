package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListHolder> {

    private ArrayList<GroceryListsModel> groceryListsModels;

    public  GroceryListAdapter(ArrayList<GroceryListsModel> groceryList){
        groceryListsModels = groceryList;
    }

    @NonNull
    @Override
    public GroceryListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grocerylist_item, viewGroup, false);

        return new GroceryListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListHolder groceryListHolder, int i) {
        groceryListHolder.bind(groceryListsModels.get(i));
    }

    @Override
    public int getItemCount() {
        return groceryListsModels.size();
    }
}
