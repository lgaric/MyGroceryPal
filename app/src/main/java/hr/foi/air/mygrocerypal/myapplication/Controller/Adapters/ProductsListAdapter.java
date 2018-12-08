package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

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

    private List<GroceryListProductsModel> listOfProducts;
    TextView totalTextView;

    public  ProductsListAdapter(List<GroceryListProductsModel> productsList, TextView total){
        listOfProducts = productsList;
        totalTextView = total;
        calculateTotalAmount();
    }

    private void calculateTotalAmount(){
        double totalAmount = 0;
        for (GroceryListProductsModel product: listOfProducts) {
            totalAmount += product.getPrice() * product.getQuantity();
        }
        totalTextView.setText(roundToTwoDecimalPlaces(totalAmount));
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
        productsListHolder.bind(listOfProducts.get(position), listOfProducts);
    }

    @Override
    public int getItemCount() {
        return listOfProducts.size();
    }

    public GroceryListProductsModel getItem(int position){
        return listOfProducts.get(position);
    }

    public List<GroceryListProductsModel> getListOfProducts(){
        return listOfProducts;
    }


    public class ProductsListHolder extends RecyclerView.ViewHolder {

        public GroceryListProductsModel product;
        public TextView productName;
        public ImageButton increaseGroceryAmount, decreaseGroceryAmount, deleteProduct;
        public EditText productQuantity;
        public List<GroceryListProductsModel> listOfProducts;

        public ProductsListHolder(@NonNull View itemView) {
            super(itemView);

            this.productName = itemView.findViewById(R.id.productName);
            this.productQuantity = itemView.findViewById(R.id.productQuantity);
            this.decreaseGroceryAmount = itemView.findViewById(R.id.groceryDecreaseAmount);
            this.increaseGroceryAmount = itemView.findViewById(R.id.groceryIncreaseAmount);
            this.deleteProduct = itemView.findViewById(R.id.groceryDelete);

            increaseGroceryAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(product == null){
                        addSelectedProductToGroceryList();
                        productQuantity.setText(Integer.toString(product.getQuantity()));
                    }else{
                        product.setQuantity(product.getQuantity() + 1);
                        productQuantity.setText(Integer.toString(product.getQuantity()));

                    }
                    calculateTotalAmount();
                }
            });

            decreaseGroceryAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    calculateTotalAmount();
                }
            });

            deleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listOfProducts.remove(product);//izbrisao iz liste
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(),listOfProducts.size());

                    calculateTotalAmount();
                }
            });

            productQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    //ako korisnik izbrise sve iz EditTexta
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

                    calculateTotalAmount();
                }
            });
        }

        private void addSelectedProductToGroceryList(){
            product = new GroceryListProductsModel();
            product.setName(product.getName());
            product.setQuantity(1);
            product.setBought(0);
            product.setProduct_key(product.getProduct_key());
            listOfProducts.add(product);
        }

        private boolean isEmpty(EditText editText){
            if(editText.getText().toString().trim().length() > 0) return false;
            else return true;
        }

        public void bind(GroceryListProductsModel product, List<GroceryListProductsModel> listOfProducts){
            this.listOfProducts = listOfProducts;
            this.product = product;
            this.productName.setText(product.getName());
            this.productQuantity.setText(Integer.toString(product.getQuantity()));
        }

    }
}