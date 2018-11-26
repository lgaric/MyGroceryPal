package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.ClickListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListHolder> {

    private ArrayList<GroceryListsModel> groceryListsModels;
    private ClickListener clickListener;

    public  GroceryListAdapter(ArrayList<GroceryListsModel> groceryList, ClickListener listener){
        this.clickListener = listener;
        groceryListsModels = groceryList;
    }

    @NonNull
    @Override
    public GroceryListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        if(groceryListsModels.get(i).getStatus() == GroceryListStatus.FINISHED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.grocerylist_item_finished, viewGroup, false);
        }
        else if(groceryListsModels.get(i).getStatus() == GroceryListStatus.ACCEPTED){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.grocerylist_item_accepted, viewGroup, false);
        }
        else{
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.grocerylist_item_created, viewGroup, false);
        }

        return new GroceryListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListHolder groceryListHolder, int i) {
        groceryListHolder.bind(groceryListsModels.get(i), this.clickListener);

    }

    @Override
    public int getItemCount() {
        return groceryListsModels.size();
    }
}
