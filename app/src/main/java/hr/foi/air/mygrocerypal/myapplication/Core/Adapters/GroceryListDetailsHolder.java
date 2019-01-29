package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListDetailsHolder extends RecyclerView.ViewHolder {

    private static String CURRENCY = " kn";

    private GroceryListProductsModel mModel;
    private TextView mNameOfProduct, mBought, mQuantity, mPrice, mTotalPrice, mCurrentPrice;
    private Button btnUpBought, btnDownBought;
    private View view;
    private GroceryListsModel mGroceryListsModel;

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnUpBought:

                    if(mModel.getBought() < mModel.getQuantity()){
                        mModel.setBought(mModel.getBought() + 1);
                        mBought.setText(v.getContext().getResources().getString(R.string.bought) + " " + Integer.toString(mModel.getBought()));
                        mCurrentPrice.setText(v.getContext().getResources().getString(R.string.currentPrice) + " " + String.format("%.2f", mModel.getBought() * mModel.getPrice()) + CURRENCY);
                        updateProductQuantityInList(mModel);
                    }

                    Log.d("GROCERYDETAILSHOLDERUP", "GUMB UP: " + mModel.getGrocery_list_key());

                    break;
                case R.id.btnDownBought:

                    if(mModel.getBought() > 0){
                        mModel.setBought(mModel.getBought() - 1);
                        mBought.setText(v.getContext().getResources().getString(R.string.bought) + " " + Integer.toString(mModel.getBought()));
                        mCurrentPrice.setText(v.getContext().getResources().getString(R.string.currentPrice) + " " + String.format("%.2f", mModel.getBought() * mModel.getPrice()) + CURRENCY);
                        updateProductQuantityInList(mModel);
                    }

                    Log.d("GROCERYDETAILSHOLDERUP", "GUMB DOWN: " + mModel.getGrocery_list_key());

                    break;
                default:
                    Log.d("GROCERYDETAILSHOLDER", "CASE: DEFAULT");

                    break;
            }
        }
    };

    /**
     * Konstruktor
     * @param itemView
     * @param groceryModel
     * @param deliverer
     */
    public GroceryListDetailsHolder(@NonNull View itemView, GroceryListsModel groceryModel, boolean deliverer) {
        super(itemView);
        view = itemView;
        this.mGroceryListsModel = groceryModel;
        this.mNameOfProduct = itemView.findViewById(R.id.name_of_productTxt);
        this.mBought = itemView.findViewById(R.id.bought);
        this.mPrice = itemView.findViewById(R.id.price);
        this.mQuantity = itemView.findViewById(R.id.qunatity);
        this.mTotalPrice = itemView.findViewById(R.id.totalprice);


        if(groceryModel.getStatus() == GroceryListStatus.ACCEPTED && !deliverer) {
            this.btnUpBought = itemView.findViewById(R.id.btnUpBought);
            this.btnDownBought = itemView.findViewById(R.id.btnDownBought);

            this.mCurrentPrice = itemView.findViewById(R.id.currentprice);

            this.btnUpBought.setOnClickListener(btnClickListener);
            this.btnDownBought.setOnClickListener(btnClickListener);
        }


    }

    /**
     * Bindanje
     * @param model
     */
    public void bind(GroceryListProductsModel model){
        this.mModel = model;

        this.mNameOfProduct.setText(model.getName());
        this.mBought.setText(view.getContext().getResources().getString(R.string.bought) + " " + Integer.toString(model.getBought()));
        this.mPrice.setText(view.getContext().getResources().getString(R.string.price) + " " + Double.toString(model.getPrice()) + CURRENCY);
        this.mQuantity.setText(view.getContext().getResources().getString(R.string.quantity) + " " + Integer.toString(model.getQuantity()));

        if (this.mCurrentPrice != null){
            this.mCurrentPrice.setText(view.getContext().getResources().getString(R.string.currentPrice) + " " + String.format("%.2f", model.getPrice() * model.getBought()) + CURRENCY);
        }
        this.mTotalPrice.setText(view.getContext().getResources().getString(R.string.totalCost) + " " + String.format("%.2f", model.getPrice() * model.getQuantity()) + CURRENCY);
    }

    /**
     * Dobivanje pozicije proizvoda na listi
     * @param mProduct
     * @return
     */
    private int getProductPositionInList(GroceryListProductsModel mProduct){
        List<GroceryListProductsModel> temp = mGroceryListsModel.getProductsModels();
        int position = 0;
        for(GroceryListProductsModel product : temp){
            if(product.getName().equals(mProduct.getName()))
                return position;
            position++;
        }
        return 0;
    }

    /**
     * Azuriranje kolicine proizvoda na listi
     * @param mModel
     */
    private void updateProductQuantityInList(GroceryListProductsModel mModel){
        mGroceryListsModel.getProductsModels().get(getProductPositionInList(mModel)).setQuantity(mModel.getQuantity());
    }
}
