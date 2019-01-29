package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

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

    private static final String CURRENCY = " KN";

    private GroceryListsModel mGroceryListsModel;

    private LinearLayout mColorTrack;

    private TextView mStore;
    private TextView mPrice;
    private TextView mCommision;
    private View view;

    //ONCLICKLISTENER -> ANDROID INTERFACE
    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGroceryListClickListener.groceryListSelected(mGroceryListsModel);
        }
    };
        private GroceryListClickListener mGroceryListClickListener;

    /**
     * Konstruktor
     * @param itemView
     */
    public GroceryListHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            mColorTrack = itemView.findViewById(R.id.color_track);

            mStore = itemView.findViewById(R.id.store_name);
            mPrice = itemView.findViewById(R.id.price);
            mCommision = itemView.findViewById(R.id.commision);

            itemView.setOnClickListener(onclick);
        }


    /**
     * Bindanje
     * @param mGroceryListsModel
     * @param mGroceryListClickListener
     */
    public void bind(GroceryListsModel mGroceryListsModel, GroceryListClickListener mGroceryListClickListener){
            this.mGroceryListsModel = mGroceryListsModel;
            mStore.setText(mGroceryListsModel.getStore_name());
            mPrice.setText(view.getContext().getResources().getString(R.string.totalCost) + " " + mGroceryListsModel.getTotal_price() + CURRENCY);
            mCommision.setText(view.getContext().getResources().getString(R.string.commisson) + " " + mGroceryListsModel.getCommision() + CURRENCY);

            setColorOfTrack();

            this.mGroceryListClickListener = mGroceryListClickListener;
        }

    /**
     * Postavljanje boje pored GL-a koja govori o njegovom statusu
     */
    private void setColorOfTrack(){
            if(mGroceryListsModel.getStatus() == GroceryListStatus.FINISHED)
                mColorTrack.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimaryDark));
            else if(mGroceryListsModel.getStatus() == GroceryListStatus.CREATED)
                mColorTrack.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorAccent));
            else
                mColorTrack.setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimary));
        }

}
