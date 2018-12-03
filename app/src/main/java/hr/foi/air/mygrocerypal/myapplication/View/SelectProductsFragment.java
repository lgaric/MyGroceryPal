package hr.foi.air.mygrocerypal.myapplication.View;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.GroceryListAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.SelectProductsAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.ChangeProductQuantityListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.SelectProductsListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.SelectProductsController;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class SelectProductsFragment extends Fragment implements SelectProductsListener, ChangeProductQuantityListener {
    private Button addProductsToGroceryList;
    private GroceryListsModel groceryListsModel;
    private SelectProductsController selectProductsController;
    private RecyclerView recyclerView;
    private SelectProductsAdapter selectProductsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_select_products, container, false);

        GroceryListsModel test = new GroceryListsModel();
        test.setStore_name("Konzum");
        groceryListsModel = test;

        //groceryListsModel = (GroceryListsModel)getArguments().getSerializable("GROCERY_LIST_MODEL");
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectProductsController = new SelectProductsController(this);

        recyclerView = view.findViewById(R.id.recycler_view);
        addProductsToGroceryList = view.findViewById(R.id.addProductsToGroceryList);
        selectProductsController.loadGroceryLists(groceryListsModel.getStore_name());

        addProductsToGroceryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Proslijedi listu u previousFragment
                List<GroceryListProductsModel> sendArray = groceryListsModel.getProductsModels();

                Bundle bundle = new Bundle();
                bundle.putSerializable("groceryListProductsModel", (Serializable) sendArray);

                // TODO pass data to previous fragment
                //CreateNewGroceryListFragment previousFragment = (CreateNewGroceryListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_create_new_grocerylist);
                //previousFragment.setArguments(bundle);
                Toast.makeText(getContext(), "Dodali ste " + sendArray.size() + " proizvoda!", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void productsListReceived(ArrayList<ProductsModel> productsList) {
        if(productsList != null){
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            selectProductsAdapter = new SelectProductsAdapter(productsList, groceryListsModel, this);
            recyclerView.setAdapter(selectProductsAdapter);
        }
    }


    @Override
    public void updateQuantity(int position, EditText productQuantity, int value) {
        ProductsModel product = selectProductsAdapter.getItem(position);

        if(value > 0) {
            productQuantity.setText( Integer.parseInt(productQuantity.getText().toString()) + 1);
            selectProductsController.addToProductsList(product, productQuantity.getText().toString());
        } else if (value < 0){
            if(Integer.parseInt(productQuantity.getText().toString()) > 0)
                productQuantity.setText( Integer.parseInt(productQuantity.getText().toString()) - 1);
            if(Integer.parseInt(productQuantity.getText().toString()) == 0)
                selectProductsController.removeFromProductsList(product);
        }
    }


    @Override
    public void manuallySetQuantity() {

    }
}
