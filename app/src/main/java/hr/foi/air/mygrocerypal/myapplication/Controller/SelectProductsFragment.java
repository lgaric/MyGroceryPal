package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.SelectProductsAdapter;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.SelectProductsListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.SelectProductsHelper;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class SelectProductsFragment extends Fragment implements SelectProductsListener{
    private List<GroceryListProductsModel> mAllreadyAddedProducts = new ArrayList<>();
    private Button btnAddProductsToGroceryList;
    private SelectProductsHelper mSelectProductsHelper;
    private RecyclerView mRecyclerView;
    private SelectProductsAdapter mSelectProductsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_select_products, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSelectProductsHelper = new SelectProductsHelper(this);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        btnAddProductsToGroceryList = view.findViewById(R.id.addProductsToGroceryList);

        mSelectProductsHelper.loadProductsByStore(getArguments().getString("store_name"));
        mAllreadyAddedProducts = (List<GroceryListProductsModel>)getArguments().getSerializable("list_of_products");

        btnAddProductsToGroceryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GroceryListProductsModel> listOfProducts = mSelectProductsAdapter.getmListOfProducts();

                //ukoliko ne postoji prethodni fragment ne čini ništa!
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    Intent intent = new Intent(getContext(), SelectProductsFragment.class);
                    intent.putExtra("groceryListOfProducts", (Serializable) listOfProducts);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public void productsListReceived(ArrayList<ProductsModel> mProductsList) {
        if(mProductsList != null){
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            InitializeAdapter(mProductsList);
            mRecyclerView.setAdapter(mSelectProductsAdapter);
        }
    }

    private void InitializeAdapter(ArrayList<ProductsModel> mProductsList){
        if(mAllreadyAddedProducts == null)
            mSelectProductsAdapter = new SelectProductsAdapter(mProductsList);
        else
            mSelectProductsAdapter = new SelectProductsAdapter(mProductsList, mAllreadyAddedProducts);
    }

}
