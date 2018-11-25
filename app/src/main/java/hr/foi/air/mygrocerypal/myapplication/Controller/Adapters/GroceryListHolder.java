package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import hr.foi.air.mygrocerypal.myapplication.Controller.ClickListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListHolder extends RecyclerView.ViewHolder {

    private static final String PRICE = "Cijena: ";
    private static final String PROVIZIJA = "Provizija: ";
    private static final String CURRENCY = " KN";

    private GroceryListsModel groceryListsModel;

    private TextView store;
    private TextView price;
    private TextView commision;

    //ONCLICKLISTENER -> ANDROID INTERFACE
    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickListener.onItemSelect(groceryListsModel);
        }
    };
    private ClickListener clickListener;

    public GroceryListHolder(@NonNull View itemView) {
        super(itemView);
        store = itemView.findViewById(R.id.store_name);
        price = itemView.findViewById(R.id.price);
        commision = itemView.findViewById(R.id.commision);
        itemView.setOnClickListener(onclick);
    }

    public void bind(GroceryListsModel groceryListsModel, ClickListener clickListener){
        this.groceryListsModel = groceryListsModel;
        store.setText(groceryListsModel.getStore_name());
        price.setText(PRICE + groceryListsModel.getTotal_price() + CURRENCY);
        commision.setText(PROVIZIJA + groceryListsModel.getCommision() + CURRENCY);

        this.clickListener = clickListener;
    }
}
