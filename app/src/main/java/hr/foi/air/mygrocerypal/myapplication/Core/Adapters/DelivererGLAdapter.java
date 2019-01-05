package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class DelivererGLAdapter extends RecyclerView.Adapter<DelivererGLHolder> {

    private ArrayList<GroceryListsModel> mGroceryListsModels;
    private GroceryListOperationListener mGroceryListOperationListener;

    /*
        0 - AKTIVNI GL DOSTAVLJAC
        1 - IGNORIRANI GL DOSTAVLJAC
        2 - PRIHVACENI GL DOSTAVLJAC
    */
    int mType;

    public DelivererGLAdapter(ArrayList<GroceryListsModel> groceryList, GroceryListOperationListener mGroceryListOperationListener, int mType){
        this.mGroceryListOperationListener = mGroceryListOperationListener;
        this.mGroceryListsModels = groceryList;
        this.mType = mType;
    }

    @NonNull
    @Override
    public DelivererGLHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        if(mType == 0) { //AKTIVNI
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.gl_deliverer_active, viewGroup, false);
        }
        else if(mType == 1){ //IGNORIRANI -> STAVI SVOJ LAYOUT
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.gl_item_ignored, viewGroup, false);
        }
        else{ //PRIHVACENI -> STAVI SVOJ LAYOUT
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.gl_item_accepted, viewGroup, false);
        }

        return new DelivererGLHolder(view, mType);
    }

    @Override
    public void onBindViewHolder(@NonNull DelivererGLHolder groceryListHolder, int i) {
        groceryListHolder.bind(this.mGroceryListsModels.get(i), this.mGroceryListOperationListener);
    }

    @Override
    public int getItemCount() {
        return mGroceryListsModels.size();
    }
}
