package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
    private List<GroceryListProductsModel> mListOfProducts;
    private List<GroceryListProductsModel> mAllreadyAddedProducts;
    private int mProductQuantity;

    public  SelectProductsAdapter(ArrayList<ProductsModel> mProductsList){
        this.mProductsList = mProductsList;
        mListOfProducts = new ArrayList<>();
    }

    public  SelectProductsAdapter(ArrayList<ProductsModel> mProductsList, List<GroceryListProductsModel> mAllreadyAddedProducts){
        this.mProductsList = mProductsList;
        mListOfProducts = new ArrayList<>();
        this.mAllreadyAddedProducts = mAllreadyAddedProducts;
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
        if(mAllreadyAddedProducts != null){
            ProductsModel allreadyAddedProduct = alleadyAddedProduct(mProductsList.get(position));
            if(allreadyAddedProduct != null)
                selectProductsHolder.bind(allreadyAddedProduct, mListOfProducts, mProductQuantity);
            else
                selectProductsHolder.bind(mProductsList.get(position), mListOfProducts);
        }else
            selectProductsHolder.bind(mProductsList.get(position), mListOfProducts);
    }

    private ProductsModel alleadyAddedProduct(ProductsModel productsModel){
        for(GroceryListProductsModel product : mAllreadyAddedProducts){
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

    public List<GroceryListProductsModel> getmListOfProducts(){
        return mListOfProducts;
    }



    public class SelectProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private GroceryListProductsModel mProduct;
        private ProductsModel mProductsModel;
        private TextView mProductName, mProductPrice;
        private ImageButton btnIncreaseGroceryAmount, btnDecreaseGroceryAmount;
        private EditText mProductQuantity;
        private List<GroceryListProductsModel> mListOfProducts;

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

        private void addSelectedProductToGroceryList(int mValue){
            mProduct = new GroceryListProductsModel();
            mProduct.setName(mProductsModel.getName());
            mProduct.setPrice(mProductsModel.getCurrent_price());
            mProduct.setQuantity(mValue);
            mProduct.setBought(0);
            mProduct.setProduct_key(mProductsModel.getProduct_key());
            mListOfProducts.add(mProduct);
        }

        View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateQuantity();
            }
        };

        private boolean isEmpty(EditText mEditText){
            if(mEditText.getText().toString().trim().length() > 0) return false;
            else return true;
        }

        public void bind(ProductsModel Product, List<GroceryListProductsModel> mListOfProducts){
            this.mListOfProducts = mListOfProducts;
            this.mProductsModel = Product;
            this.mProductName.setText(Product.getName());
            this.mProductPrice.setText(Double.toString(Product.getCurrent_price()));
        }

        public void bind(ProductsModel mPproduct, List<GroceryListProductsModel> mListOfProducts, int mProductQuantity){
            this.mListOfProducts = mListOfProducts;
            this.mProductsModel = mPproduct;
            this.mProductName.setText(mPproduct.getName());
            this.mProductPrice.setText(Double.toString(mPproduct.getCurrent_price()));
            this.mProductQuantity.setText(Integer.toString(mProductQuantity));
            addSelectedProductToGroceryList(mProductQuantity);
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

        private void increaseAmount(){
            if(mProduct == null){
                addSelectedProductToGroceryList(1);
                mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
            }else{
                mProduct.setQuantity(mProduct.getQuantity() + 1);
                mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
            }
        }

        private void decreaseAmount(){
            if(mProduct != null){
                if(mProduct.getQuantity() > 1){
                    mProduct.setQuantity(mProduct.getQuantity() - 1);
                    mProductQuantity.setText(Integer.toString(mProduct.getQuantity()));
                }
                else{
                    mListOfProducts.remove(mProduct);
                    mProduct = null;
                    mProductQuantity.setText("0");
                }
            }
        }

        private void updateQuantity(){
            if(isEmpty(mProductQuantity)){
                if(mProduct != null){
                    mListOfProducts.remove(mProduct);
                    mProduct = null;
                }
                mProductQuantity.setText("0");
            }else if (Integer.parseInt(mProductQuantity.getText().toString()) == 0 && mProduct != null){
                mListOfProducts.remove(mProduct);
                mProduct = null;
            }else if(Integer.parseInt(mProductQuantity.getText().toString()) > 0 && mProduct != null)
                mProduct.setQuantity(Integer.parseInt(mProductQuantity.getText().toString()));
        }
    }
}
