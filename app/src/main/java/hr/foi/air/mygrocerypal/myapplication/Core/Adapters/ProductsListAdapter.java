package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ProductsListHolder> {

    private List<GroceryListProductsModel> mListOfAddedProducts;//nova verzija
    TextView mTotalTextView;

    public  ProductsListAdapter(List<GroceryListProductsModel> productsList, TextView total){
        mListOfAddedProducts = productsList;
        mTotalTextView = total;
        calculateTotalAmount();
    }

    private void calculateTotalAmount(){
        double totalAmount = 0;
        for (GroceryListProductsModel product: mListOfAddedProducts) {
            totalAmount += product.getPrice() * product.getQuantity();
        }
        mTotalTextView.setText(roundToTwoDecimalPlaces(totalAmount));
    }

    private String roundToTwoDecimalPlaces(double value){
        double roundedValue = Math.round(value*100)/100.00;
        return Double.toString(roundedValue);
    }

    @NonNull
    @Override
    public ProductsListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item_with_delete, viewGroup, false);

        return new ProductsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsListHolder productsListHolder, int position) {
        productsListHolder.bind(mListOfAddedProducts.get(position), mListOfAddedProducts);
    }

    @Override
    public int getItemCount() {
        return mListOfAddedProducts.size();
    }

    public GroceryListProductsModel getItem(int position){
        return mListOfAddedProducts.get(position);
    }

    public List<GroceryListProductsModel> getmListOfAddedProducts(){
        return mListOfAddedProducts;
    }


    public class ProductsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private GroceryListProductsModel mProduct;
        private TextView mProductName;
        private ImageButton btnIncreaseGroceryAmount, btnDecreaseGroceryAmount, btnDeleteProduct;
        private EditText mProductQuantity;
        private List<GroceryListProductsModel> mListOfAddedProducts;

        public ProductsListHolder(@NonNull View itemView) {
            super(itemView);

            this.mProductName = itemView.findViewById(R.id.productName);
            this.mProductQuantity = itemView.findViewById(R.id.productQuantity);
            this.btnDecreaseGroceryAmount = itemView.findViewById(R.id.btnGroceryDecreaseAmount);
            this.btnIncreaseGroceryAmount = itemView.findViewById(R.id.btnGroceryIncreaseAmount);
            this.btnDeleteProduct = itemView.findViewById(R.id.groceryDelete);

            btnIncreaseGroceryAmount.setOnClickListener(this);
            btnDecreaseGroceryAmount.setOnClickListener(this);
            btnDeleteProduct.setOnClickListener(this);
            mProductQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    updateQuantity();
                }
            });
        }


        private boolean isEmpty(EditText mEditText){
            if(mEditText.getText().toString().trim().length() > 0) return false;
            else return true;
        }

        public void bind(GroceryListProductsModel mProduct, List<GroceryListProductsModel> mListOfProducts){
            this.mListOfAddedProducts = mListOfProducts;
            this.mProduct = mProduct;

            this.mProductName.setText(mProduct.getName());
            this.mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
        }

        private int getProductPositionInList(GroceryListProductsModel mProduct){
            int position = 0;
            for(GroceryListProductsModel product : mListOfAddedProducts){
                if(product == mProduct)
                    return position;
                position++;
            }
            return 0;
        }

        /**
         * Povecaj kolicinu trenutnog proizvoda za 1
         */
        private void increaseAmount(){
            if(mProduct != null){
                mProduct.setQuantity(mProduct.getQuantity() + 1);
                mListOfAddedProducts.get(getProductPositionInList(mProduct)).setQuantity(mProduct.getQuantity());
                mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
            }
            calculateTotalAmount();

        }

        /**
         * Smanji kolicinu trenutnog proizvoda za 1
         */
        private void decreaseAmount(){
            if(mProduct != null && mProduct.getQuantity() > 0){
                if(mProduct.getQuantity() > 1){
                    mProduct.setQuantity(mProduct.getQuantity() - 1);
                    mListOfAddedProducts.get(getProductPositionInList(mProduct)).setQuantity(mProduct.getQuantity());
                    mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
                }
                else{
                    deleteGroceryFromList();
                }
            }
            calculateTotalAmount();

        }

        /**
         * Rucna izmjena kolicine proizvoda
         */
        private void updateQuantity(){
            int currentQuantity = Integer.parseInt(mProductQuantity.getText().toString());

            // ako korisnik upise 0 ili izbrise sve iz editTexta
            if ((currentQuantity == 0 && mProduct != null) || isEmpty(mProductQuantity)){
                deleteGroceryFromList();
            }else if(currentQuantity > 0 && mProduct != null){
                mProduct.setQuantity(currentQuantity);
                mListOfAddedProducts.get(getProductPositionInList(mProduct)).setQuantity(mProduct.getQuantity());
            }
            calculateTotalAmount();
        }

        private void deleteGroceryFromList(){
            mListOfAddedProducts.remove(getProductPositionInList(mProduct));//izbrisao iz liste
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), mListOfAddedProducts.size());
            calculateTotalAmount();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnGroceryDecreaseAmount:
                    decreaseAmount();
                    break;
                case R.id.btnGroceryIncreaseAmount:
                    increaseAmount();
                    break;
                case R.id.groceryDelete:
                    deleteGroceryFromList();
                default:
                    break;
            }
        }
    }
}