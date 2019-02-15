package hr.foi.air.mygrocerypal.myapplication.Controller;


import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.GroceryListDetailsAdapter;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.GroceryListDetailsHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListDetailsListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.PaymentHelper.PaymentActivity;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ShowGroceryListDetailsFragment extends Fragment implements GroceryListDetailsListener {
    private static String CURRENCY = " kn";
    private GroceryListDetailsHelper mProductsController;
    private GroceryListsModel mGroceryListsModel;
    private UserModel mDelivererModel;
    private LinearLayout mColorOfHeaderGroceryDetails;
    private TextView mStoreName, mFirstName, mTotalPrice, mCommision,  mContact, mOrderID, mDate;
    private RecyclerView mRecyclerView;
    private Button btnAgainCommit;
    private ProgressBar mProgressBar;
    private Boolean mDeliverer = false;

    /**
     * Inicijalizacija
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mGroceryListsModel = (GroceryListsModel)getArguments().getSerializable("GROCERY_LIST_MODEL");
        mProductsController =  new GroceryListDetailsHelper(this);
        mProductsController.loadGroceryListProducts(mGroceryListsModel);
        if(getArguments().containsKey("IS_DELIVERER"))
            mDeliverer = getArguments().getBoolean("IS_DELIVERER");

        return inflater.inflate(R.layout.fragment_show_grocery_list_details, container, false);
    }

    /**
     * Povezivanje s XML-om
     * @param view
     * @param savedInstanceState
     */
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
        mOrderID = view.findViewById(R.id.order_id);
        mDate = view.findViewById(R.id.order_date);

        //LISTVIEW
        mRecyclerView = view.findViewById(R.id.listOfItemsGroceryDetails);

        mProgressBar = view.findViewById(R.id.progress);

        //BUTTON
        btnAgainCommit = view.findViewById(R.id.againCommitbtn);
        btnAgainCommit.setOnClickListener(btnListenerAgainCommit);

        setHeaderColor(mColorOfHeaderGroceryDetails);
        setButtonText(btnAgainCommit);
        setGroceryListDetailsHeader();
    }

    /**
     * Postavljanje headera za GL-ove
     */
    private void setGroceryListDetailsHeader(){
        mStoreName.append(mGroceryListsModel.getStore_name());
        mOrderID.append(mGroceryListsModel.getGrocerylist_key());
        mDate.append(mGroceryListsModel.getStart_date());
        mTotalPrice.append(mGroceryListsModel.getTotal_price() + CURRENCY);
        mCommision.append(mGroceryListsModel.getCommision() + CURRENCY);
    }

    /**
     * Postavljane teksta pojedinog buttona
     * @param mButtonText
     */
    private void setButtonText(Button mButtonText){
        if(mGroceryListsModel.getStatus() != GroceryListStatus.FINISHED)
            mButtonText.setText(getActivity().getResources().getString(R.string.confirmCaps));

        if(mGroceryListsModel.getStatus() == GroceryListStatus.CREATED || mDeliverer)
            mButtonText.setVisibility(View.GONE);
    }

    /**
     * Promjena boje za pojedini GL
     * @param mLayout
     */
    private void setHeaderColor(LinearLayout mLayout){
        if(mGroceryListsModel.getStatus() == GroceryListStatus.ACCEPTED)
            mLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        else if(mGroceryListsModel.getStatus() == GroceryListStatus.FINISHED)
            mLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        else
            mLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    /**
     * Dobivanje popisa stavki GL-a
     * @param mGroceryListUser
     * @param mGroceryListProducts
     */
    @Override
    public void groceryListDetailsReceived(@Nullable UserModel mGroceryListUser, ArrayList<GroceryListProductsModel> mGroceryListProducts) {
        if(mGroceryListProducts != null) {
            mProgressBar.setVisibility(View.GONE);
            mGroceryListsModel.setProductsModels(mGroceryListProducts);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            GroceryListDetailsAdapter v2 = new GroceryListDetailsAdapter(mGroceryListsModel, mDeliverer);
            mRecyclerView.setAdapter(v2);
        }

        if (mGroceryListUser != null){
            mDelivererModel = mGroceryListUser;
            mFirstName.append(mGroceryListUser.getFirst_name() + " " + mGroceryListUser.getLast_name());
            mContact.append(mGroceryListUser.getPhone_number());
        }else{
            mFirstName.append("-");
            mContact.append("-");
        }
    }

    /**
     * Click listener
     */
    private View.OnClickListener btnListenerAgainCommit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doOperation();
        }
    };

    /**
     * Napravi operaciju na pritisak gumba
     * Ako je na gumbu text Ponovi, ponavlja se GL
     * Inace izvrsi transakciju
     */
    private void doOperation(){
        String buttonText = btnAgainCommit.getText().toString();
        if(buttonText.equals(getResources().getString(R.string.repeatCaps))) {
            CreateNewGroceryListFragment createNewGroceryListFragment = new CreateNewGroceryListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("repeatGL", mGroceryListsModel);
            createNewGroceryListFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, createNewGroceryListFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else{
            Double totalPrice = Double.parseDouble(mGroceryListsModel.getCommision())
                    + Double.parseDouble(mGroceryListsModel.getTotal_price());
            Intent i = new Intent(getActivity(), PaymentActivity.class);
            i.putExtra("USER_MODEL", mDelivererModel);
            i.putExtra("TOTAL_PAYMENT", totalPrice);
            i.putExtra("MODEL_GL", mGroceryListsModel);
            startActivity(i);
        }
    }
}
