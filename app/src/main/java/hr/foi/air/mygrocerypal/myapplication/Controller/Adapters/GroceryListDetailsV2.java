package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListDetailsV2 extends RecyclerView.Adapter<GroceryListDetailsHolder>  {

    GroceryListsModel groceryList;

    public GroceryListDetailsV2(GroceryListsModel groceryList){
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public GroceryListDetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = null;

        if(groceryList.getStatus() != GroceryListStatus.FINISHED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.grocerylistdetails_item_buttons, viewGroup, false);
        }
        else{
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.listview_association, viewGroup, false);
        }

        return new GroceryListDetailsHolder(view, groceryList);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListDetailsHolder groceryListHolder, int i) {
        groceryListHolder.bind(groceryList.getProductsModels().get(i));
    }

    @Override
    public int getItemCount() {
        return groceryList.getProductsModels().size();
    }
}