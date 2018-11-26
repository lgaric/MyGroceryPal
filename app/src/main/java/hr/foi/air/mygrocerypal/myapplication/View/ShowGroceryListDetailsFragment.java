package hr.foi.air.mygrocerypal.myapplication.View;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListProductsController;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListUserController;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListUserListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Temp;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGroceryListDetailsFragment extends Fragment implements GroceryListProductsListener, GroceryListUserListener {
    private GroceryListProductsController productsController;
    private GroceryListUserController userController;
    private String id;
    private TextView textView;
    private TextView textView2;
    private TextView textView3;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        id = getArguments().getString("groceryListKey");
//        Temp.groceryListsModel.getGrocerylist_key();
        productsController =  new GroceryListProductsController(this, Temp.groceryListsModel.getGrocerylist_key());
        userController = new GroceryListUserController(this, Temp.groceryListsModel.getUser_accepted_id());

        return inflater.inflate(R.layout.fragment_show_grocery_list_details, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        productsController.loadGroceryProductsLists(id);
        textView = view.findViewById(R.id.textOfItemsGroceryDetails);
        textView2 = view.findViewById(R.id.firstnameGroceryDetails);
        textView3 = view.findViewById(R.id.lastnameGroceryDetails);
    }

    @Override
    public void groceryListProductsReceived(ArrayList<GroceryListProductsModel> groceryListProducts) {
//        Toast.makeText(getActivity(), groceryListProducts.size(), Toast.LENGTH_LONG).show();

        if(groceryListProducts != null){
            textView.append("\n");
            for (int i = 0; i<groceryListProducts.size(); i++){
                textView.append(getString(R.string.tab) + groceryListProducts.get(i).getName() + "\n");
            }
        }
    }

    @Override
    public void groceryListUserReceived(UserModel groceryListUser) {

        if (groceryListUser != null){
            textView2.append(groceryListUser.getFirst_name());
            textView3.append(groceryListUser.getLast_name());

        }


    }
}
