package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class DelivererGLHolder extends RecyclerView.ViewHolder {

    private static final String PRICE = "Cijena: ";
    private static final String FEE = "Provizija: ";
    private static final String CURRENCY = " KN";

    private GroceryListsModel groceryListsModel;

    private TextView store;
    private TextView price;
    private TextView commision;
    private TextView address;
    private TextView delivery_town;

    Button acceptGL;
    Button ignoreGL;
    Button returnGL;

    private GroceryListOperationListener groceryListOperationListener;

    //PROSLIJEDI FRAGMENTU ODABRANU INSTANCU GroceryListModel-a
    private View.OnClickListener groceryListDetails = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            groceryListOperationListener.buttonPressedOnGroceryList(groceryListsModel, GroceryListOperation.DETAILS);
        }
    };

    //GUMBOVI NA CARDVIEW (PRIHVATI, IGNORIRAJ, VRATI)
    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.accept_gl:
                    groceryListOperationListener.buttonPressedOnGroceryList(groceryListsModel, GroceryListOperation.ACCEPT);
                    break;
                case R.id.ignore_gl:
                    groceryListOperationListener.buttonPressedOnGroceryList(groceryListsModel, GroceryListOperation.IGNORE);
                    break;
                case R.id.btnRestoreIgnored: //CASE ZA VRATI
                    groceryListOperationListener.buttonPressedOnGroceryList(groceryListsModel, GroceryListOperation.RETURN);
            }
        }
    };

    public DelivererGLHolder(@NonNull View itemView, int type) {
        super(itemView);

        Log.d("TROLL", Integer.toString(itemView.getId()));
        //POHRANI SVE POTREBNE INFORMACIJE
        this.store = itemView.findViewById(R.id.store_name);
        this.price = itemView.findViewById(R.id.price);
        this.commision = itemView.findViewById(R.id.commision);
        this.address = itemView.findViewById(R.id.address);
        this.delivery_town = itemView.findViewById(R.id.delivery_town);

        setButtonsListener(type, itemView);

        //AKO NEKO PRITISNE NA GROCERYLIST I ŽELI VIDJETI DETALJE(OVO SE NE ODNOSI NA GUMB)
        itemView.setOnClickListener(groceryListDetails);
    }

    private void setButtonsListener(int type, View itemView){
        switch (type){
            case 0: //AKTIVNI GROCERYLISTI
                acceptGL = itemView.findViewById(R.id.accept_gl);
                ignoreGL = itemView.findViewById(R.id.ignore_gl);
                acceptGL.setOnClickListener(buttonClickListener);
                ignoreGL.setOnClickListener(buttonClickListener);
                break;
            case 1:
                //VRATI IGNORIRAI GL
                returnGL = itemView.findViewById(R.id.btnRestoreIgnored);//PRONADI SVOJ GUMB
                returnGL.setOnClickListener(buttonClickListener);//DODAJ MU CLICKLISTENER
                break;
        }
    }

    public void bind(GroceryListsModel groceryListsModel, GroceryListOperationListener groceryListOperationListener){
        this.groceryListsModel = groceryListsModel;

        //POSTAVI VRIJEDNOST TEXTVIEWOVA
        this.store.setText(groceryListsModel.getStore_name());
        this.price.setText(PRICE + groceryListsModel.getTotal_price() + CURRENCY);
        this.commision.setText(FEE + groceryListsModel.getCommision() + CURRENCY);
        this.address.setText("ADRESA: " + groceryListsModel.getDelivery_address());
        this.delivery_town.setText("GRAD: " + groceryListsModel.getDelivery_town());

        this.groceryListOperationListener = groceryListOperationListener;
    }

}
