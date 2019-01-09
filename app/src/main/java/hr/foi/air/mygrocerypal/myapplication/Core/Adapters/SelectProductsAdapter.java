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

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class SelectProductsAdapter extends RecyclerView.Adapter<SelectProductsAdapter.SelectProductsHolder> {

    private ArrayList<ProductsModel> mProductsList;
    private List<GroceryListProductsModel> mListOfAddedProducts;
    private int mProductQuantity = 0;

    public  SelectProductsAdapter(ArrayList<ProductsModel> mProductsList, List<GroceryListProductsModel> mListOfAddedProducts){
        this.mProductsList = mProductsList;
        this.mListOfAddedProducts = mListOfAddedProducts;
    }

    @NonNull
    @Override
    public SelectProductsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item, viewGroup, false);

        return new SelectProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectProductsHolder selectProductsHolder, int position) {
        if(mListOfAddedProducts != null){
            ProductsModel allreadyAddedProduct = alleadyAddedProduct(mProductsList.get(position));
            if(allreadyAddedProduct != null)
                selectProductsHolder.bind(allreadyAddedProduct, mListOfAddedProducts, mProductQuantity);
            else
                selectProductsHolder.bind(mProductsList.get(position), mListOfAddedProducts, 0);
        }else
            selectProductsHolder.bind(mProductsList.get(position), mListOfAddedProducts, 0);
    }

    private ProductsModel alleadyAddedProduct(ProductsModel productsModel){
        for(GroceryListProductsModel product : mListOfAddedProducts){
            if(productsModel.getProduct_key().equals(product.getProduct_key())){
                mProductQuantity = product.getQuantity();
                return productsModel;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mProductsList.size();
    }

    public ProductsModel getItem(int position){
        return mProductsList.get(position);
    }


    public class SelectProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private GroceryListProductsModel mProduct;
        private ProductsModel mProductsModel;
        private TextView mProductName, mProductPrice;
        private ImageButton btnIncreaseGroceryAmount, btnDecreaseGroceryAmount;
        private EditText mProductQuantity;
        private List<GroceryListProductsModel> mListOfAddedProducts;

        public SelectProductsHolder(@NonNull View itemView) {
            super(itemView);

            this.mProductName = itemView.findViewById(R.id.productName);
            this.mProductPrice = itemView.findViewById(R.id.productPrice);
            this.mProductQuantity = itemView.findViewById(R.id.productQuantity);
            this.btnDecreaseGroceryAmount = itemView.findViewById(R.id.btnGroceryDecreaseAmount);
            this.btnIncreaseGroceryAmount = itemView.findViewById(R.id.btnGroceryIncreaseAmount);

            btnIncreaseGroceryAmount.setOnClickListener(this);
            btnDecreaseGroceryAmount.setOnClickListener(this);
            mProductQuantity.setOnFocusChangeListener(mOnFocusChangeListener);
        }

        private void bindProduct(int mProductQuantity){
            mProduct = new GroceryListProductsModel();
            mProduct.setName(mProductsModel.getName());
            mProduct.setPrice(mProductsModel.getCurrent_price());
            mProduct.setQuantity(mProductQuantity);
            mProduct.setBought(0);
            mProduct.setProduct_key(mProductsModel.getProduct_key());
        }

        private void addSelectedProductToGroceryList(int mProductQuantity){
            bindProduct(mProductQuantity);
            mListOfAddedProducts.add(mProduct);
        }

        View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateQuantity();
            }
        };


        public void bind(ProductsModel mProduct, List<GroceryListProductsModel> mListOfAddedProducts, int mProductQuantity){
            this.mListOfAddedProducts = mListOfAddedProducts;
            this.mProductsModel = mProduct;
            this.mProductName.setText(mProduct.getName());
            this.mProductPrice.setText(Double.toString(mProduct.getCurrent_price()));
            if(mProductQuantity != 0){
                this.mProductQuantity.setText(Integer.toString(mProductQuantity));
                bindProduct(mProductQuantity);
            }
            else
                this.mProductQuantity.setText("0");
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
                default:
                    break;
            }
        }

        /**
         * Povecaj kolicinu trenutnog proizvoda za 1
         */
        private void increaseAmount(){
            if(mProduct == null){
                addSelectedProductToGroceryList(1);
                mProductQuantity.setText("1");
            }else{
                mProduct.setQuantity(mProduct.getQuantity() + 1);
                mListOfAddedProducts.get(getProductPositionInList(mProduct)).setQuantity(mProduct.getQuantity());
                mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
            }

        }

        /**
         * Smanji kolicinu trenutnog proizvoda za 1
         */
        private void decreaseAmount(){
            if(mProduct != null){
                if(mProduct.getQuantity() > 1){
                    mProduct.setQuantity(mProduct.getQuantity() - 1);
                    mListOfAddedProducts.get(getProductPositionInList(mProduct)).setQuantity(mProduct.getQuantity());
                    mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
                }
                else{
                    mListOfAddedProducts.remove(getProductPositionInList(mProduct));
                    mProduct = null;
                    mProductQuantity.setText("0");
                }
            }

        }

        private int getProductPositionInList(GroceryListProductsModel mProduct){
            int position = 0;
            for(GroceryListProductsModel product : mListOfAddedProducts){
                if(product.getName().equals(mProduct.getName()))
                    return position;
                position++;
            }
            return 0;
        }

        /**
         * Rucna izmjena kolicine proizvoda
         */
        private void updateQuantity(){
            int currentQuantity = Integer.parseInt(mProductQuantity.getText().toString());

            //ako korisnik izbrise sve iz EditTexta
            if(isEmpty(mProductQuantity)){
                if(mProduct != null){
                    mProduct.setQuantity(0);
                    mListOfAddedProducts.remove(getProductPositionInList(mProduct));
                }
                mProductQuantity.setText("0");
            }else if (currentQuantity == 0 && mProduct != null){
                mProduct.setQuantity(0);
                mListOfAddedProducts.remove(getProductPositionInList(mProduct));
                mProductQuantity.setText("0");
            }else if(currentQuantity > 0 && mProduct != null){
                mProduct.setQuantity(currentQuantity);
                mListOfAddedProducts.get(getProductPositionInList(mProduct)).setQuantity(mProduct.getQuantity());
            }else if(currentQuantity > 0 && mProduct == null){
                addSelectedProductToGroceryList(currentQuantity);
                mProductQuantity.setText(Integer.toString(currentQuantity));
            }
        }

        private boolean isEmpty(EditText mEditText){
            if(mEditText.getText().toString().trim().length() > 0) return false;
            else return true;
        }
    }
}
