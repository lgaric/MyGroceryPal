package hr.foi.air.mygrocerypal.myapplication.Controller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class GroceryListDetailsAdapter extends ArrayAdapter<GroceryListProductsModel> {

    private static String BOUGHT = "Kupljeno: ";
    private static String QUANTITY = "Količina: ";
    private static String PRICE = "Cijena proizvoda: ";
    private static String CURRENTPRICE = "Trenutna cijena: ";
    private static String TOTALPRICE = "Ukupna cijena: ";
    private static String CURRENCY = "kn";

    private static class GroceryListDetailsHolder {
         String key_of_product;
         TextView name, bought, price, quantity, totalPrice;
    }

    public GroceryListDetailsAdapter(Context context, ArrayList<GroceryListProductsModel> groceryProducts){
        super(context, 0, groceryProducts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        GroceryListProductsModel product = getItem(position);
        GroceryListDetailsHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.listview_association, parent, false);

            viewHolder = new GroceryListDetailsHolder();
            viewHolder.name = convertView.findViewById(R.id.name_of_productTxt);
            viewHolder.bought = convertView.findViewById(R.id.bought);
            viewHolder.price = convertView.findViewById(R.id.price);
            viewHolder.quantity = convertView.findViewById(R.id.qunatity);
            viewHolder.totalPrice = convertView.findViewById(R.id.totalprice);
//            viewHolder.currentPrice = convertView.findViewById(R.id.currentprice);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (GroceryListDetailsHolder) convertView.getTag();
        }

        viewHolder.key_of_product = product.getGrocery_list_key();
        viewHolder.name.setText(product.getName());
        viewHolder.bought.setText(BOUGHT + Integer.toString(product.getBought()));
        viewHolder.price.setText(PRICE + Double.toString(product.getPrice()) + CURRENCY);
        viewHolder.quantity.setText(QUANTITY + Integer.toString(product.getQuantity()));
//        viewHolder.currentPrice.setText(CURRENTPRICE + String.format("%.2f", product.getPrice() * product.getBought()) + CURRENCY);
        viewHolder.totalPrice.setText(TOTALPRICE + String.format("%.2f", product.getPrice() * product.getQuantity()) + CURRENCY);

        return convertView;
    }
}
