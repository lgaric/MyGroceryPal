package hr.foi.air.mygrocerypal.myapplication.Core.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class DelivererGLHolder extends RecyclerView.ViewHolder {

    private static final String PRICE = "Cijena: ";
    private static final String FEE = "Provizija: ";
    private static final String CURRENCY = " KN";

    private GroceryListsModel groceryListsModel;

    private TextView mStore;
    private TextView mPrice;
    private TextView mCommision;
    private TextView mAddress;
    private TextView mDeliveryTown;

    Button btnAcceptGL;
    Button btnIgnoreGL;
    Button btnReturnGL;

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
                    break;
                default:
                    break;
            }
        }
    };

    public DelivererGLHolder(@NonNull View itemView, int type) {
        super(itemView);

        Log.d("TROLL", Integer.toString(itemView.getId()));
        //POHRANI SVE POTREBNE INFORMACIJE
        this.mStore = itemView.findViewById(R.id.store_name);
        this.mPrice = itemView.findViewById(R.id.price);
        this.mCommision = itemView.findViewById(R.id.commision);
        this.mAddress = itemView.findViewById(R.id.address);
        this.mDeliveryTown = itemView.findViewById(R.id.delivery_town);

        setButtonsListener(type, itemView);

        //AKO NEKO PRITISNE NA GROCERYLIST I ŽELI VIDJETI DETALJE(OVO SE NE ODNOSI NA GUMB)
        itemView.setOnClickListener(groceryListDetails);
    }

    private void setButtonsListener(int type, View itemView){
        switch (type){
            case 0: //AKTIVNI GROCERYLISTI
                btnAcceptGL = itemView.findViewById(R.id.accept_gl);
                btnIgnoreGL = itemView.findViewById(R.id.ignore_gl);
                btnAcceptGL.setOnClickListener(buttonClickListener);
                btnIgnoreGL.setOnClickListener(buttonClickListener);
                break;
            case 1:
                //VRATI IGNORIRAI GL
                btnReturnGL = itemView.findViewById(R.id.btnRestoreIgnored);//PRONADI SVOJ GUMB
                btnReturnGL.setOnClickListener(buttonClickListener);//DODAJ MU CLICKLISTENER
                break;
            default:
                break;
        }
    }

    public void bind(GroceryListsModel groceryListsModel, GroceryListOperationListener groceryListOperationListener){
        this.groceryListsModel = groceryListsModel;

        //POSTAVI VRIJEDNOST TEXTVIEWOVA
        this.mStore.setText(groceryListsModel.getStore_name());
        this.mPrice.setText(PRICE + groceryListsModel.getTotal_price() + CURRENCY);
        this.mCommision.setText(FEE + groceryListsModel.getCommision() + CURRENCY);
        this.mAddress.setText("ADRESA: " + groceryListsModel.getDelivery_address());
        this.mDeliveryTown.setText("GRAD: " + groceryListsModel.getDelivery_town());

        this.groceryListOperationListener = groceryListOperationListener;
    }

}
