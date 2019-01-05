package hr.foi.air.mygrocerypal.myapplication.Controller;


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

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.GroceryListDetailsAdapter;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.GroceryListDetailsHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListDetailsListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ShowGroceryListDetailsFragment extends Fragment implements GroceryListDetailsListener {

    private static String CURRENCY = " kn";

    private GroceryListDetailsHelper mProductsController;
    private GroceryListsModel mGroceryListsModel;

    private LinearLayout mColorOfHeaderGroceryDetails;

    private TextView mStoreName, mFirstName, mTotalPrice, mCommision,  mContact;
    private RecyclerView mRecyclerView;

    private Button btnAgainCommit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mGroceryListsModel = (GroceryListsModel)getArguments().getSerializable("GROCERY_LIST_MODEL");
        mProductsController =  new GroceryListDetailsHelper(this);
        mProductsController.loadGroceryListProducts(mGroceryListsModel);

        return inflater.inflate(R.layout.fragment_show_grocery_list_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //LINEARLAYOUT
        mColorOfHeaderGroceryDetails = view.findViewById(R.id.colorOfHeaderGroceryDetails);

        //EDITTEXT
        mStoreName = view.findViewById(R.id.storenameGroceryDetails);
        mFirstName = view.findViewById(R.id.firstnameGroceryDetails);
        mTotalPrice = view.findViewById(R.id.priceGroceryDetails);
        mCommision = view.findViewById(R.id.commisionGroceryDetails);
        mContact = view.findViewById(R.id.contactGroceryDetails);

        //LISTVIEW
        mRecyclerView = view.findViewById(R.id.listOfItemsGroceryDetails);

        //BUTTON
        btnAgainCommit = view.findViewById(R.id.againCommitbtn);
        btnAgainCommit.setOnClickListener(btnListenerAgainCommit);

        setHeaderColor(mColorOfHeaderGroceryDetails);
        setButtonText(btnAgainCommit);
        setGroceryListDetailsHeader();
    }

    private void setGroceryListDetailsHeader(){
        mStoreName.append(mGroceryListsModel.getStore_name());
        mTotalPrice.append(mGroceryListsModel.getTotal_price() + CURRENCY);
        mCommision.append(mGroceryListsModel.getCommision() + CURRENCY);
    }

    private void setButtonText(Button mButtonText){
        if(mGroceryListsModel.getStatus() != GroceryListStatus.FINISHED)
            mButtonText.setText("POTVRDI");

        if(mGroceryListsModel.getStatus() == GroceryListStatus.CREATED)
            mButtonText.setVisibility(View.GONE);
    }

    private void setHeaderColor(LinearLayout mLayout){
        if(mGroceryListsModel.getStatus() == GroceryListStatus.ACCEPTED)
            mLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        else if(mGroceryListsModel.getStatus() == GroceryListStatus.FINISHED)
            mLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        else
            mLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void groceryListDetailsReceived(@Nullable UserModel mGroceryListUser, ArrayList<GroceryListProductsModel> mGroceryListProducts) {
        if(mGroceryListProducts != null) {
            mGroceryListsModel.setProductsModels(mGroceryListProducts);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            GroceryListDetailsAdapter v2 = new GroceryListDetailsAdapter(mGroceryListsModel, false);
            mRecyclerView.setAdapter(v2);
        }

        if (mGroceryListUser != null){
            mFirstName.append(mGroceryListUser.getFirst_name() + " " + mGroceryListUser.getLast_name());
            mContact.append(mGroceryListUser.getPhone_number());
        }else{
            mFirstName.append("-");
            mContact.append("-");
        }
    }

    private View.OnClickListener btnListenerAgainCommit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateNewGroceryListFragment createNewGroceryListFragment = new CreateNewGroceryListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("repeatGL", mGroceryListsModel);
            createNewGroceryListFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, createNewGroceryListFragment)
                    .addToBackStack(null)
                    .commit();
        }
    };
}
