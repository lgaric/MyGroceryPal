package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.ClickListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class DelivererGLAdapter extends RecyclerView.Adapter<DelivererGLHolder> {

    private ArrayList<GroceryListsModel> groceryListsModels;
    private GroceryListOperationListener groceryListOperationListener;

    /*
        0 - AKTIVNI GL DOSTAVLJAC
        1 - IGNORIRANI GL DOSTAVLJAC
        2 - PRIHVACENI GL DOSTAVLJAC
    */
    int type;

    public DelivererGLAdapter(ArrayList<GroceryListsModel> groceryList, GroceryListOperationListener groceryListOperationListener, int type){
        this.groceryListOperationListener = groceryListOperationListener;
        this.groceryListsModels = groceryList;
        this.type = type;
    }

    @NonNull
    @Override
    public DelivererGLHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        if(type == 0) { //AKTIVNI
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.gl_deliverer_active, viewGroup, false);
        }
        else if(type == 1){ //IGNORIRANI -> STAVI SVOJ LAYOUT
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.grocerylist_item_created, viewGroup, false);
        }
        else{ //PRIHVACENI -> STAVI SVOJ LAYOUT
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.grocerylist_item_created, viewGroup, false);
        }

        return new DelivererGLHolder(view, type);
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererGLHolder groceryListHolder, int i) {
        groceryListHolder.bind(this.groceryListsModels.get(i), this.groceryListOperationListener);
    }

    @Override
    public int getItemCount() {
        return groceryListsModels.size();
    }
}
