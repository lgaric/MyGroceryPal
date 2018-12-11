package hr.foi.air.mygrocerypal.myapplication.View;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.GroceryListDetailsAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListProductsController;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListUserController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListUserListener;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ShowGroceryListDetailsFragment extends Fragment implements GroceryListProductsListener, GroceryListUserListener {

    private static String CURRENCY = " kn";

    private GroceryListProductsController productsController;
    private GroceryListUserController userController;
    private GroceryListsModel groceryListsModel;

    private LinearLayout colorOfHeaderGroceryDetails;
    private boolean groceryListAccepted = true;

    private TextView storeNametxt;
    private TextView firstNametxt;
    private TextView totalPricetxt;
    private TextView commisiontxt;
    private TextView phoneNumbertxt;

    private RecyclerView recyclerView;

    private Button againCommitbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groceryListsModel = (GroceryListsModel)getArguments().getSerializable("GROCERY_LIST_MODEL");
        productsController =  new GroceryListProductsController(this, groceryListsModel.getGrocerylist_key());
        userController = new GroceryListUserController(this, groceryListsModel.getUser_accepted_id());

        return inflater.inflate(R.layout.fragment_show_grocery_list_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //LINEARLAYOUT
        colorOfHeaderGroceryDetails = view.findViewById(R.id.colorOfHeaderGroceryDetails);

        //EDITTEXT
        storeNametxt = view.findViewById(R.id.storenameGroceryDetails);
        firstNametxt = view.findViewById(R.id.firstnameGroceryDetails);
        totalPricetxt = view.findViewById(R.id.priceGroceryDetails);
        commisiontxt = view.findViewById(R.id.commisionGroceryDetails);
        phoneNumbertxt = view.findViewById(R.id.contactGroceryDetails);

        //LISTVIEW
        recyclerView = view.findViewById(R.id.listOfItemsGroceryDetails);

        //BUTTON
        againCommitbtn = view.findViewById(R.id.againCommitbtn);

        setHeaderColor(colorOfHeaderGroceryDetails);
        setButtonText(againCommitbtn);
        setGroceryListDetailsHeader();

        if(!groceryListAccepted){
            firstNametxt.append("-");
            phoneNumbertxt.append("-");
        }
    }

    private void setGroceryListDetailsHeader(){
        storeNametxt.append(groceryListsModel.getStore_name());
        totalPricetxt.append(groceryListsModel.getTotal_price() + CURRENCY);
        commisiontxt.append(groceryListsModel.getCommision() + CURRENCY);
    }

    private void setButtonText(Button buttonText){
        if(groceryListsModel.getStatus() != GroceryListStatus.FINISHED)
            buttonText.setText("POTVRDI");
    }

    private void setHeaderColor(LinearLayout layout){
        if(groceryListsModel.getStatus() == GroceryListStatus.ACCEPTED)
            layout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        else if(groceryListsModel.getStatus() == GroceryListStatus.FINISHED)
            layout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        else
            layout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void groceryListProductsReceived(ArrayList<GroceryListProductsModel> groceryListProducts) {
        if(groceryListProducts != null) {
            groceryListsModel.setProductsModels(groceryListProducts);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            GroceryListDetailsAdapter v2 = new GroceryListDetailsAdapter(groceryListsModel, false);
            recyclerView.setAdapter(v2);
        }

    }

    @Override
    public void groceryListUserReceived(@Nullable UserModel groceryListUser) {
        if (groceryListUser != null){
            firstNametxt.append(groceryListUser.getFirst_name() + " " + groceryListUser.getLast_name());
            phoneNumbertxt.append(groceryListUser.getPhone_number());
        }else{
            groceryListAccepted = false;
        }
    }
}
