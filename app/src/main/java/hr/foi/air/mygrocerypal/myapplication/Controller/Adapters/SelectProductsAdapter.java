package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.ChangeProductQuantityListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class SelectProductsAdapter extends RecyclerView.Adapter<SelectProductsAdapter.SelectProductsHolder> {

    private ArrayList<ProductsModel> productsList;
    private GroceryListsModel groceryListsModel;
    private ChangeProductQuantityListener changeProductQuantityListener;

    public  SelectProductsAdapter(ArrayList<ProductsModel> productsList, GroceryListsModel groceryListsModel, ChangeProductQuantityListener listener){
        this.changeProductQuantityListener = listener;
        this.productsList = productsList;
        this.groceryListsModel = groceryListsModel;

        //TODO fix this
        this.groceryListsModel.getProductsModels();
    }

    @NonNull
    @Override
    public SelectProductsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item, viewGroup, false);

        return new SelectProductsHolder(view, this.changeProductQuantityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectProductsHolder selectProductsHolder, int position) {
        selectProductsHolder.bind(productsList.get(position), groceryListsModel);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public ProductsModel getItem(int position){
        return productsList.get(position);
    }






    public class SelectProductsHolder extends RecyclerView.ViewHolder {
        ChangeProductQuantityListener clickListener;

        public GroceryListProductsModel product;
        public GroceryListsModel groceryListsModel;
        public ProductsModel productsModel;
        public TextView productName, productPrice;
        public ImageButton increaseGroceryAmount, decreaseGroceryAmount;
        public EditText productQuantity;
        public List<GroceryListProductsModel> listOfProducts;

        public SelectProductsHolder(@NonNull View itemView, ChangeProductQuantityListener listener) {
            super(itemView);
            this.clickListener = listener;
            listOfProducts = new ArrayList<>();

            this.productName = itemView.findViewById(R.id.productName);
            this.productPrice = itemView.findViewById(R.id.productPrice);
            this.productQuantity = itemView.findViewById(R.id.productQuantity);
            this.decreaseGroceryAmount = itemView.findViewById(R.id.groceryDecreaseAmount);
            this.increaseGroceryAmount = itemView.findViewById(R.id.groceryIncreaseAmount);

            increaseGroceryAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(product == null){
                        product = new GroceryListProductsModel();
                        product.setName(productsModel.getName());
                        product.setPrice(productsModel.getCurrent_price());
                        product.setQuantity(1);
                        product.setBought(0);
                        product.setProduct_key(productsModel.getProduct_key());
                        groceryListsModel.getProductsModels().add(product);
                        productQuantity.setText(Integer.toString(product.getQuantity()));
                    }else{
                        product.setQuantity(product.getQuantity() + 1);
                        productQuantity.setText(Integer.toString(product.getQuantity()));

                    }
                }
            });

            decreaseGroceryAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if(product != null){
                    int currentQuantity = product.getQuantity();
                    if(currentQuantity > 1){
                        product.setQuantity(product.getQuantity() - 1);
                        productQuantity.setText(Integer.toString(product.getQuantity()));
                    }
                    else{
                        groceryListsModel.getProductsModels().remove(product);
                        product = null;
                        productQuantity.setText("0");
                    }
                }
                }
            });

            productQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(isEmpty(productQuantity)){
                        if(product != null){
                            groceryListsModel.getProductsModels().remove(product);
                            product = null;
                        }
                        productQuantity.setText("0");
                    }else if (Integer.parseInt(productQuantity.getText().toString()) == 0 && product != null){
                        groceryListsModel.getProductsModels().remove(product);
                        product = null;
                    }else if(Integer.parseInt(productQuantity.getText().toString()) > 0 && product != null)
                        product.setQuantity(Integer.parseInt(productQuantity.getText().toString()));
                }
            });
        }



        private boolean isEmpty(EditText editText){
            if(editText.getText().toString().trim().length() > 0) return false;
            else return true;
        }

        public void bind(ProductsModel product, GroceryListsModel groceryListsModel){
            this.groceryListsModel = groceryListsModel;
            this.productsModel = product;
            this.productName.setText(product.getName());
            this.productPrice.setText(Double.toString(product.getCurrent_price()));
        }

    }
}
