package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.filter.FilterManager;
import com.example.filter.FilterableObject;
import com.example.filter.ObjectsFilterListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.SelectProductsAdapter;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.SelectProductsListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.SelectProductsHelper;
import hr.foi.air.mygrocerypal.myapplication.Model.CategoriesModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class SelectProductsFragment extends Fragment implements SelectProductsListener, ObjectsFilterListener {
    //lista vec dodanih proizvoda
    public List<GroceryListProductsModel> mListOfAddedProducts = new ArrayList<>();

    private Button btnAddProductsToGroceryList;
    private SelectProductsHelper mSelectProductsHelper;
    private RecyclerView mRecyclerView;
    private SelectProductsAdapter mSelectProductsAdapter;
    private ArrayList<ProductsModel> mProductsList;
    private TextView mNoneProducts;
    private ImageButton mChangeSearchingType;
    private ArrayList<String> categories = new ArrayList<>();

    //pohrana liste filtriranih proizvoda
    ArrayList<ProductsModel> filteredList = new ArrayList<>();

    FilterManager filterManager = new FilterManager();

    /**
     * Inicijalizacija
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_select_products, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.addProducts));
        return rootView;
    }

    /**
     * Nastavljanje fragmenta
     */
    @Override
    public void onResume() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.addProducts));
        super.onResume();
    }

    /**
     * Inicijalizacija
     * @param view
     * @param savedInstanceState
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSelectProductsHelper = new SelectProductsHelper(this);

        mChangeSearchingType = view.findViewById(R.id.changeSearcingType);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        btnAddProductsToGroceryList = view.findViewById(R.id.addProductsToGroceryList);
        mNoneProducts = view.findViewById(R.id.noneProducts);
        mSelectProductsHelper.loadProductCategories();
        mSelectProductsHelper.loadProductsByStore(getArguments().getString("store_name"));
        mListOfAddedProducts = (List<GroceryListProductsModel>)getArguments().getSerializable("list_of_products");

        mChangeSearchingType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSearchingType();
            }
        });

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
    }


    /**
     * promijeni nacin trazenja proizvoda
     */
    void changeSearchingType(){
        if(categories != null && mProductsList != null) {
            Fragment nextFragment = filterManager.getNextFragment(mProductsList, this, categories);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.modul, nextFragment);
            transaction.commit();
        }
    }

    /**
     * dohvati listu proizvoda odabrane trgovine
     * @param mProductsList
     */
    @Override
    public void productsListByStoreReceived(ArrayList<ProductsModel> mProductsList) {
        if (mProductsList != null) {
            this.mProductsList = mProductsList;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            inflateAdapter(mProductsList);
            setTextVisibility(mProductsList);
            changeSearchingType();
        }
    }

    /**
     * dohvati listu svih kategorija
     * @param mCategoriesList
     */
    @Override
    public void categoriesListReceived(ArrayList<CategoriesModel> mCategoriesList) {
        if(mCategoriesList != null){
            for(int i = 0; i < mCategoriesList.size(); i++)
                categories.add(mCategoriesList.get(i).getName());
            changeSearchingType();
        }
    }

    /**
     * proslijedi već dodane proizvode u adapter ako postoje
     * @param mProductsList
     */
    private void initializeAdapter(ArrayList<ProductsModel> mProductsList){
        if(mListOfAddedProducts == null)
            mListOfAddedProducts = new ArrayList<>();
        mSelectProductsAdapter = new SelectProductsAdapter(mProductsList, mListOfAddedProducts);
    }

    /**
     * ispisi sve proizvode u recycler view
     * @param mProductsList
     */
    private void inflateAdapter(ArrayList<ProductsModel> mProductsList){
        mRecyclerView.setAdapter(null);
        initializeAdapter(mProductsList);
        mRecyclerView.setAdapter(mSelectProductsAdapter);
    }

    /**
     * Prikazuje tekst korisniku da ne postoje proizvodi po zadanom kriteriju
     * @param list
     */
    private void setTextVisibility(ArrayList<ProductsModel> list){
        if(list.size() > 0)
            mNoneProducts.setVisibility(View.GONE);
        else
            mNoneProducts.setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void listIsFiltered(ArrayList<? extends FilterableObject> listOfObjects) {
        filteredList = (ArrayList<ProductsModel>) listOfObjects;
        inflateAdapter(filteredList);
        setTextVisibility(filteredList);
    }
}