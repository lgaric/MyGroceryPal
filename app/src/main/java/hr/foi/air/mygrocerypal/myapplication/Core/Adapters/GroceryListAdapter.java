package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListClickListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListHolder> {

    private ArrayList<GroceryListsModel> mGroceryListsModels;
    private GroceryListClickListener mGroceryListClickListener;

    /**
     * Konstruktor
     * @param groceryList
     * @param listener
     */
    public  GroceryListAdapter(ArrayList<GroceryListsModel> groceryList, GroceryListClickListener listener){
        this.mGroceryListClickListener = listener;
        mGroceryListsModels = groceryList;
    }

    /**
     * Inicijalizacija
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public GroceryListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grocerylist_item_created, viewGroup, false);

        return new GroceryListHolder(view);
    }

    /**
     * Bindanje view holdera
     * @param groceryListHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull GroceryListHolder groceryListHolder, int i) {
        groceryListHolder.bind(mGroceryListsModels.get(i), this.mGroceryListClickListener);

    }

    /**
     * Dobivanje broja GL-ova
     * @return
     */
    @Override
    public int getItemCount() {
        return mGroceryListsModels.size();
    }
}
