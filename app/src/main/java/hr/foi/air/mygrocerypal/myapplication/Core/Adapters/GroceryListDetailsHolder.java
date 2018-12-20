package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
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
    private TextView nameOfProduct, bought, quantity, price, totalPrice, currentPrice;
    private Button upBought, downBought;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.upBought:

                    if(model.getBought() < model.getQuantity()){
                        model.setBought(model.getBought() + 1);
                        bought.setText(BOUGHT + Integer.toString(model.getBought()));
                        currentPrice.setText(CURRENTPRICE + String.format("%.2f", model.getBought() * model.getPrice()) + CURRENCY);
                    }

                    Log.d("GROCERYDETAILSHOLDERUP", "GUMB UP: " + model.getGrocery_list_key());

                    break;
                case R.id.downBought:

                    if(model.getBought() > 0){
                        model.setBought(model.getBought() - 1);
                        bought.setText(BOUGHT + Integer.toString(model.getBought()));
                        currentPrice.setText(CURRENTPRICE + String.format("%.2f", model.getBought() * model.getPrice()) + CURRENCY);
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

        this.nameOfProduct = itemView.findViewById(R.id.name_of_productTxt);
        this.bought = itemView.findViewById(R.id.bought);
        this.price = itemView.findViewById(R.id.price);
        this.quantity = itemView.findViewById(R.id.qunatity);
        this.totalPrice = itemView.findViewById(R.id.totalprice);


        if(groceryModel.getStatus() == GroceryListStatus.ACCEPTED) {
            this.upBought = itemView.findViewById(R.id.upBought);
            this.downBought = itemView.findViewById(R.id.downBought);

            this.currentPrice = itemView.findViewById(R.id.currentprice);

            this.upBought.setOnClickListener(clickListener);
            this.downBought.setOnClickListener(clickListener);
        }


    }

    public void bind(GroceryListProductsModel model){
        this.model = model;

        this.nameOfProduct.setText(model.getName());
        this.bought.setText(BOUGHT + Integer.toString(model.getBought()));
        this.price.setText(PRICE + Double.toString(model.getPrice()) + CURRENCY);
        this.quantity.setText(QUANTITY + Integer.toString(model.getQuantity()));

        if (this.currentPrice != null){
            this.currentPrice.setText(CURRENTPRICE + String.format("%.2f", model.getPrice() * model.getBought()) + CURRENCY);
        }
        this.totalPrice.setText(TOTALPRICE + String.format("%.2f", model.getPrice() * model.getQuantity()) + CURRENCY);
    }
}
