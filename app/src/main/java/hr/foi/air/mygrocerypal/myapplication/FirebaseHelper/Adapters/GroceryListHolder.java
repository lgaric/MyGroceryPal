package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.ClickListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListHolder extends RecyclerView.ViewHolder {

    private static final String PRICE = "Cijena: ";
    private static final String FEE = "Provizija: ";
    private static final String CURRENCY = " KN";

    private GroceryListsModel groceryListsModel;

    private LinearLayout color_track;

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

            color_track = itemView.findViewById(R.id.color_track);

            store = itemView.findViewById(R.id.store_name);
            price = itemView.findViewById(R.id.price);
            commision = itemView.findViewById(R.id.commision);

            itemView.setOnClickListener(onclick);
        }

        public void bind(GroceryListsModel groceryListsModel, ClickListener clickListener){
            this.groceryListsModel = groceryListsModel;
            store.setText(groceryListsModel.getStore_name());
            price.setText(PRICE + groceryListsModel.getTotal_price() + CURRENCY);
            commision.setText(FEE + groceryListsModel.getCommision() + CURRENCY);

            setColorOfTrack();

            this.clickListener = clickListener;
        }

        private void setColorOfTrack(){
            if(groceryListsModel.getStatus() == GroceryListStatus.FINISHED)
                color_track.setBackgroundColor(Color.parseColor("#0E314F"));
            else if(groceryListsModel.getStatus() == GroceryListStatus.CREATED)
                color_track.setBackgroundColor(Color.parseColor("#D81B60"));
            else
                color_track.setBackgroundColor(Color.parseColor("#447eb1"));
        }

}
