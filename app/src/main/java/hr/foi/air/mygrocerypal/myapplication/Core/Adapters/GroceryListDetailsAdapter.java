package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListDetailsAdapter extends RecyclerView.Adapter<GroceryListDetailsHolder>  {

    GroceryListsModel groceryList;
    boolean deliverer;

    public GroceryListDetailsAdapter(GroceryListsModel groceryList, boolean deliverer){
        this.groceryList = groceryList;
        this.deliverer = deliverer;
    }

    @NonNull
    @Override
    public GroceryListDetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        if(!deliverer) {
            if (groceryList.getStatus() == GroceryListStatus.ACCEPTED) {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.grocerylistdetails_item_buttons, viewGroup, false);
            } else {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_association, viewGroup, false);
            }
        }else {
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
