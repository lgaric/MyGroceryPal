package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class SelectProductsAdapter extends RecyclerView.Adapter<SelectProductsAdapter.SelectProductsHolder> {

    private ArrayList<ProductsModel> productsList;
    private List<GroceryListProductsModel> listOfProducts;
    private List<GroceryListProductsModel> allreadyAddedProducts;
    private int productQuantity;

    public  SelectProductsAdapter(ArrayList<ProductsModel> productsList){
        this.productsList = productsList;
        listOfProducts = new ArrayList<>();
    }

    public  SelectProductsAdapter(ArrayList<ProductsModel> productsList, List<GroceryListProductsModel> allreadyAddedProducts){
        this.productsList = productsList;
        listOfProducts = new ArrayList<>();
        this.allreadyAddedProducts = allreadyAddedProducts;
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
        if(allreadyAddedProducts != null){
            ProductsModel allreadyAddedProduct = alleadyAddedProduct(productsList.get(position));
            if(allreadyAddedProduct != null)
                selectProductsHolder.bind(allreadyAddedProduct, listOfProducts, productQuantity);
            else
                selectProductsHolder.bind(productsList.get(position), listOfProducts);
        }else
            selectProductsHolder.bind(productsList.get(position), listOfProducts);
    }

    private ProductsModel alleadyAddedProduct(ProductsModel productsModel){
        for(GroceryListProductsModel product : allreadyAddedProducts){
            if(productsModel.getProduct_key().equals(product.getProduct_key())){
                productQuantity = product.getQuantity();
                return productsModel;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public ProductsModel getItem(int position){
        return productsList.get(position);
    }

    public List<GroceryListProductsModel> getListOfProducts(){
        return listOfProducts;
    }



    public class SelectProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public GroceryListProductsModel product;
        public ProductsModel productsModel;
        public TextView productName, productPrice;
        public ImageButton increaseGroceryAmount, decreaseGroceryAmount;
        public EditText productQuantity;
        public List<GroceryListProductsModel> listOfProducts;

        public SelectProductsHolder(@NonNull View itemView) {
            super(itemView);

            this.productName = itemView.findViewById(R.id.productName);
            this.productPrice = itemView.findViewById(R.id.productPrice);
            this.productQuantity = itemView.findViewById(R.id.productQuantity);
            this.decreaseGroceryAmount = itemView.findViewById(R.id.groceryDecreaseAmount);
            this.increaseGroceryAmount = itemView.findViewById(R.id.groceryIncreaseAmount);

            increaseGroceryAmount.setOnClickListener(this);
            decreaseGroceryAmount.setOnClickListener(this);
            productQuantity.setOnFocusChangeListener(onFocusChangeListener);
        }

        private void addSelectedProductToGroceryList(int value){
            product = new GroceryListProductsModel();
            product.setName(productsModel.getName());
            product.setPrice(productsModel.getCurrent_price());
            product.setQuantity(value);
            product.setBought(0);
            product.setProduct_key(productsModel.getProduct_key());
            listOfProducts.add(product);
        }

        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateQuantity();
            }
        };

        private boolean isEmpty(EditText editText){
            if(editText.getText().toString().trim().length() > 0) return false;
            else return true;
        }

        public void bind(ProductsModel product, List<GroceryListProductsModel> listOfProducts){
            this.listOfProducts = listOfProducts;
            this.productsModel = product;
            this.productName.setText(product.getName());
        }

        public void bind(ProductsModel product, List<GroceryListProductsModel> listOfProducts, int productQuantity){
            this.listOfProducts = listOfProducts;
            this.productsModel = product;
            this.productName.setText(product.getName());
            this.productPrice.setText(Double.toString(product.getCurrent_price()));
            this.productQuantity.setText(Integer.toString(productQuantity));
            addSelectedProductToGroceryList(productQuantity);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.groceryDecreaseAmount:
                    decreaseAmount();
                    break;
                case R.id.groceryIncreaseAmount:
                    increaseAmount();
                    break;
            }
        }

        private void increaseAmount(){
            if(product == null){
                addSelectedProductToGroceryList(1);
                productQuantity.setText(Integer.toString(product.getQuantity()));
            }else{
                product.setQuantity(product.getQuantity() + 1);
                productQuantity.setText(Integer.toString(product.getQuantity()));
            }
        }

        private void decreaseAmount(){
            if(product != null){
                if(product.getQuantity() > 1){
                    product.setQuantity(product.getQuantity() - 1);
                    productQuantity.setText(Integer.toString(product.getQuantity()));
                }
                else{
                    listOfProducts.remove(product);
                    product = null;
                    productQuantity.setText("0");
                }
            }
        }

        private void updateQuantity(){
            if(isEmpty(productQuantity)){
                if(product != null){
                    listOfProducts.remove(product);
                    product = null;
                }
                productQuantity.setText("0");
            }else if (Integer.parseInt(productQuantity.getText().toString()) == 0 && product != null){
                listOfProducts.remove(product);
                product = null;
            }else if(Integer.parseInt(productQuantity.getText().toString()) > 0 && product != null)
                product.setQuantity(Integer.parseInt(productQuantity.getText().toString()));
        }
    }
}
