package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListDetailsHolder extends RecyclerView.ViewHolder {

    private static String BOUGHT = "Kupljeno: ";
    private static String QUANTITY = "Količina: ";
    private static String PRICE = "Cijena proizvoda: ";
    private static String CURRENTPRICE = "Trenutna cijena: ";
    private static String TOTALPRICE = "Ukupna cijena: ";
    private static String CURRENCY = " kn";

    private GroceryListProductsModel model;
    private TextView mNameOfProduct, mBought, mQuantity, mPrice, mTotalPrice, mCurrentPrice;
    private Button btnUpBought, btnDownBought;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.upBought:

                    if(model.getBought() < model.getQuantity()){
                        model.setBought(model.getBought() + 1);
                        mBought.setText(BOUGHT + Integer.toString(model.getBought()));
                        mCurrentPrice.setText(CURRENTPRICE + String.format("%.2f", model.getBought() * model.getPrice()) + CURRENCY);
                    }

                    Log.d("GROCERYDETAILSHOLDERUP", "GUMB UP: " + model.getGrocery_list_key());

                    break;
                case R.id.downBought:

                    if(model.getBought() > 0){
                        model.setBought(model.getBought() - 1);
                        mBought.setText(BOUGHT + Integer.toString(model.getBought()));
                        mCurrentPrice.setText(CURRENTPRICE + String.format("%.2f", model.getBought() * model.getPrice()) + CURRENCY);
                    }

                    Log.d("GROCERYDETAILSHOLDERUP", "GUMB DOWN: " + model.getGrocery_list_key());

                    break;
                default:
                    Log.d("GROCERYDETAILSHOLDER", "CASE: DEFAULT");

                    break;
            }
        }
    };

    public GroceryListDetailsHolder(@NonNull View itemView, GroceryListsModel groceryModel) {
        super(itemView);

        this.mNameOfProduct = itemView.findViewById(R.id.name_of_productTxt);
        this.mBought = itemView.findViewById(R.id.bought);
        this.mPrice = itemView.findViewById(R.id.price);
        this.mQuantity = itemView.findViewById(R.id.qunatity);
        this.mTotalPrice = itemView.findViewById(R.id.totalprice);


        if(groceryModel.getStatus() == GroceryListStatus.ACCEPTED) {
            this.btnUpBought = itemView.findViewById(R.id.upBought);
            this.btnDownBought = itemView.findViewById(R.id.downBought);

            this.mCurrentPrice = itemView.findViewById(R.id.currentprice);

            this.btnUpBought.setOnClickListener(clickListener);
            this.btnDownBought.setOnClickListener(clickListener);
        }


    }

    public void bind(GroceryListProductsModel model){
        this.model = model;

        this.mNameOfProduct.setText(model.getName());
        this.mBought.setText(BOUGHT + Integer.toString(model.getBought()));
        this.mPrice.setText(PRICE + Double.toString(model.getPrice()) + CURRENCY);
        this.mQuantity.setText(QUANTITY + Integer.toString(model.getQuantity()));

        if (this.mCurrentPrice != null){
            this.mCurrentPrice.setText(CURRENTPRICE + String.format("%.2f", model.getPrice() * model.getBought()) + CURRENCY);
        }
        this.mTotalPrice.setText(TOTALPRICE + String.format("%.2f", model.getPrice() * model.getQuantity()) + CURRENCY);
    }
}
