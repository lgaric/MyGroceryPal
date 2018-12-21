package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListClickListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListHolder extends RecyclerView.ViewHolder {

    private static final String PRICE = "Cijena: ";
    private static final String FEE = "Provizija: ";
    private static final String CURRENCY = " KN";

    private GroceryListsModel mGroceryListsModel;

    private LinearLayout mColorTrack;

    private TextView mStore;
    private TextView mPrice;
    private TextView mCommision;

    //ONCLICKLISTENER -> ANDROID INTERFACE
    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGroceryListClickListener.groceryListSelected(mGroceryListsModel);
        }
    };
        private GroceryListClickListener mGroceryListClickListener;

    public GroceryListHolder(@NonNull View itemView) {
            super(itemView);

            mColorTrack = itemView.findViewById(R.id.color_track);

            mStore = itemView.findViewById(R.id.store_name);
            mPrice = itemView.findViewById(R.id.price);
            mCommision = itemView.findViewById(R.id.commision);

            itemView.setOnClickListener(onclick);
        }

        public void bind(GroceryListsModel mGroceryListsModel, GroceryListClickListener mGroceryListClickListener){
            this.mGroceryListsModel = mGroceryListsModel;
            mStore.setText(mGroceryListsModel.getStore_name());
            mPrice.setText(PRICE + mGroceryListsModel.getTotal_price() + CURRENCY);
            mCommision.setText(FEE + mGroceryListsModel.getCommision() + CURRENCY);

            setColorOfTrack();

            this.mGroceryListClickListener = mGroceryListClickListener;
        }

        private void setColorOfTrack(){
            if(mGroceryListsModel.getStatus() == GroceryListStatus.FINISHED)
                mColorTrack.setBackgroundColor(Color.parseColor("#0E314F"));
            else if(mGroceryListsModel.getStatus() == GroceryListStatus.CREATED)
                mColorTrack.setBackgroundColor(Color.parseColor("#D81B60"));
            else
                mColorTrack.setBackgroundColor(Color.parseColor("#447eb1"));
        }

}
