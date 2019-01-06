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
import android.widget.SearchView;

import com.example.filter.FilterObjects;

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
    //lista vec dodanih proizvoda
    public List<GroceryListProductsModel> mListOfAddedProducts = new ArrayList<>();

    private Button btnAddProductsToGroceryList;
    private SelectProductsHelper mSelectProductsHelper;
    private RecyclerView mRecyclerView;
    private SelectProductsAdapter mSelectProductsAdapter;
    private ArrayList<ProductsModel> mProductsList;
    private SearchView mSearchView;
    //private Spinner mSpinner;

    //pohrana liste filtriranih proizvoda
    ArrayList<ProductsModel> filteredList = new ArrayList<>();

    //genericka klasa za filtriranje proizvoda
    FilterObjects<ProductsModel> filterObjects = new FilterObjects<>();


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
        mSearchView = view.findViewById(R.id.searchView);

/*
        mSpinner = view.findViewById(R.id.spinner);
        ArrayList<String> temp = new ArrayList<>();
        temp.add("Pića");
        temp.add("Meso");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, temp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);*/

        mSelectProductsHelper.loadProductsByStore(getArguments().getString("store_name"));
        mListOfAddedProducts = (List<GroceryListProductsModel>)getArguments().getSerializable("list_of_products");

        btnAddProductsToGroceryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ukoliko ne postoji prethodni fragment ne čini ništa!
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    Intent intent = new Intent(getContext(), SelectProductsFragment.class);
                    intent.putExtra("groceryListOfProducts", (Serializable) mListOfAddedProducts);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    getFragmentManager().popBackStack();
                }
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mProductsList != null){
                    filteredList = filterObjects.filterListByNames(mProductsList, newText);
                    inflateAdapter(filteredList, true);
                }

                return false;
            }
        });
/*
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filteredList = filterObjects.filterListByCategories(mProductsList, "Pića");
                inflateAdapter(filteredList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    // dohvati listu proizvoda odabrane trgovine
    @Override
    public void productsListReceived(ArrayList<ProductsModel> mProductsList) {
        if (mProductsList != null) {
            this.mProductsList = mProductsList;
            inflateAdapter(mProductsList, false);
        }
    }

    // proslijedi već dodane proizvode u adapter ako postoje
    private void InitializeAdapter(ArrayList<ProductsModel> mProductsList, boolean filtered){
        if(mListOfAddedProducts == null) mListOfAddedProducts = new ArrayList<>();
        mSelectProductsAdapter = new SelectProductsAdapter(mProductsList, mListOfAddedProducts, filtered);
    }

    // ispisi sve proizvode u recycler view
    private void inflateAdapter(ArrayList<ProductsModel> mProductsList, boolean filtered){
        mRecyclerView.setAdapter(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        InitializeAdapter(mProductsList, filtered);
        mRecyclerView.setAdapter(mSelectProductsAdapter);
    }
}