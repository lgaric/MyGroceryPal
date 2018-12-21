package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    private List<GroceryListProductsModel> mListOfProducts;//nova verzija
    TextView mTotalTextView;

    public  ProductsListAdapter(List<GroceryListProductsModel> productsList, TextView total){
        mListOfProducts = productsList;
        mTotalTextView = total;
        calculateTotalAmount();
    }

    private void calculateTotalAmount(){
        double totalAmount = 0;
        for (GroceryListProductsModel product: mListOfProducts) {
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
        productsListHolder.bind(mListOfProducts.get(position), mListOfProducts);
    }

    @Override
    public int getItemCount() {
        return mListOfProducts.size();
    }

    public GroceryListProductsModel getItem(int position){
        return mListOfProducts.get(position);
    }

    public List<GroceryListProductsModel> getmListOfProducts(){
        return mListOfProducts;
    }


    public class ProductsListHolder extends RecyclerView.ViewHolder {

        private GroceryListProductsModel mProduct;
        private TextView mProductName;
        private ImageButton btnIncreaseGroceryAmount, btnDecreaseGroceryAmount, btnDeleteProduct;
        private EditText mProductQuantity;
        private List<GroceryListProductsModel> mListOfProducts;

        public ProductsListHolder(@NonNull View itemView) {
            super(itemView);

            this.mProductName = itemView.findViewById(R.id.productName);
            this.mProductQuantity = itemView.findViewById(R.id.productQuantity);
            this.btnDecreaseGroceryAmount = itemView.findViewById(R.id.btnGroceryDecreaseAmount);
            this.btnIncreaseGroceryAmount = itemView.findViewById(R.id.btnGroceryIncreaseAmount);
            this.btnDeleteProduct = itemView.findViewById(R.id.groceryDelete);

            btnIncreaseGroceryAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if(mProduct != null){
                      mProduct.setQuantity(mProduct.getQuantity() + 1);
                      mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));

                      calculateTotalAmount();
                  }

                }
            });

            btnDecreaseGroceryAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mProduct != null && mProduct.getQuantity() >= 1){
                            mProduct.setQuantity(mProduct.getQuantity() - 1);
                            mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
                    }
                    calculateTotalAmount();
                }
            });

            btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListOfProducts.remove(mProduct);//izbrisao iz liste
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mListOfProducts.size());

                    calculateTotalAmount();
                }
            });

            mProductQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    //ako korisnik izbrise sve iz EditTexta
                    if(isEmpty(mProductQuantity)){
                        if(mProduct != null){
                            mProduct.setQuantity(0);
                        }
                        mProductQuantity.setText("0");
                    }else if (Integer.parseInt(mProductQuantity.getText().toString()) == 0 && mProduct != null){
                        mProduct.setQuantity(0);
                    }else if(Integer.parseInt(mProductQuantity.getText().toString()) > 0 && mProduct != null)
                        mProduct.setQuantity(Integer.parseInt(mProductQuantity.getText().toString()));

                    calculateTotalAmount();
                }
            });
        }


        private boolean isEmpty(EditText mEditText){
            if(mEditText.getText().toString().trim().length() > 0) return false;
            else return true;
        }

        public void bind(GroceryListProductsModel mProduct, List<GroceryListProductsModel> mListOfProducts){
            this.mListOfProducts = mListOfProducts;
            this.mProduct = mProduct;

            this.mProductName.setText(mProduct.getName());
            this.mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
        }

    }
}