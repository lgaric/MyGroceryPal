package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.ProductsListAdapter;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.CreateNewGroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.AddGroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;
import hr.foi.air.mygrocerypal.myapplication.PaymentHelper.PaymentActivity;
import hr.foi.air.mygrocerypal.myapplication.R;

public class CreateNewGroceryListFragment extends Fragment implements AddGroceryListListener, View.OnClickListener {

    private CreateNewGroceryListHelper mCreateNewGroceryListHelper;

    private String mSelectedStoreName;
    private GroceryListsModel mGroceryListsModel;
    private List<GroceryListProductsModel> mGroceryListProductsModels = new ArrayList<>();
    private ProductsListAdapter mProductsListAdapter;
    boolean mSended = false;
    boolean mFirstEntry = false;
    boolean mRepeat = false;
    int mPositionInSpinner;
    int mFlag = 0;


    //widgets
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton;
    private EditText mAddress, mTown, mCommission;
    private TextView mStartDate, mTotalPriceAmount, mLabelProducts;
    private Button btnAddProducts, btnConfirm;
    private Spinner mSpinnerStores;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_grocerylist, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.addGroceryList));

        mTotalPriceAmount = view.findViewById(R.id.TotalPriceAmount);
        btnAddProducts = view.findViewById(R.id.btnAddProducts);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        mCommission = view.findViewById(R.id.commision);
        mStartDate = view.findViewById(R.id.startDate);
        mSpinnerStores = view.findViewById(R.id.spinnerStores);
        mAddress = view.findViewById(R.id.address);
        mTown = view.findViewById(R.id.town);
        mRecyclerView = view.findViewById(R.id.recycler_view_products);
        mAddress.setText(CurrentUser.getCurrentUser.getAddress());
        mTown.setText(CurrentUser.getCurrentUser.getTown());
        mAddress.setEnabled(false);
        mTown.setEnabled(false);
        mRadioGroup = view.findViewById(R.id.rgroup);
        mLabelProducts = view.findViewById(R.id.labelProducts);

        mRadioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        btnAddProducts.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        mSpinnerStores.setOnItemSelectedListener(onItemSelectedListener);

        //PONOVI
        if(getArguments() != null && !mRepeat){
            mGroceryListsModel = (GroceryListsModel)getArguments().getSerializable("repeatGL");
            mGroceryListProductsModels = mGroceryListsModel.getProductsModels();
            mRepeat = true;
        }
        return view;
    }

    @Override
    public void onResume() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.addGroceryList));
        super.onResume();
    }


    //PONOVI
    private void fillDataOnRepeatGL(){

        Adapter adapter = mSpinnerStores.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++){
            if(adapter.getItem(i).toString().equals(mGroceryListsModel.getStore_name())){
                mSpinnerStores.setSelection(i);
                break;
            }
        }
        mCommission.setText(mGroceryListsModel.getCommision());
        mGroceryListProductsModels = mGroceryListsModel.getProductsModels();
        if(CurrentUser.getCurrentUser.getAddress().equals(mGroceryListsModel.getDelivery_address()) && CurrentUser.getCurrentUser.getTown().equals(mGroceryListsModel.getDelivery_town())){
            mRadioGroup.check(R.id.radioButton);
        }
        else{
            mRadioGroup.check(R.id.radioButton2);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode==1 && resultCode==Activity.RESULT_OK){
            mGroceryListProductsModels = (List<GroceryListProductsModel>) data.getSerializableExtra("groceryListOfProducts");
            mFirstEntry = true;
        }

    }

    @Override
    public void onStart() {

        super.onStart();
        if(mGroceryListProductsModels.size() > 0){
            productsListReceived(mGroceryListProductsModels);
            mLabelProducts.setVisibility(View.VISIBLE);
        }else{
            mLabelProducts.setVisibility(View.GONE);
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mStartDate.setText(getDate());
        mCreateNewGroceryListHelper = new CreateNewGroceryListHelper(this);

        mCreateNewGroceryListHelper.getAllStores();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btnAddProducts:
                SelectProductsFragment selectProductsFragment = new SelectProductsFragment();
                Bundle bundle = new Bundle();
                hideKeyboard(btnAddProducts);
                mSelectedStoreName = mSpinnerStores.getSelectedItem().toString();
                bundle.putString("store_name", mSelectedStoreName);
                if(mGroceryListProductsModels.size() > 0)
                    bundle.putSerializable("list_of_products", (Serializable) mGroceryListProductsModels);
                selectProductsFragment.setArguments(bundle);
                selectProductsFragment.setTargetFragment(CreateNewGroceryListFragment.this, 1);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectProductsFragment)
                        .addToBackStack(null)
                        .commit();
                mSended = true;

                break;
            case R.id.btnConfirm:
                boolean entered = checkData();
                hideKeyboard(btnConfirm);
                if(entered){
                    createGroceryList();
                    mCreateNewGroceryListHelper.saveGroceryListWithProducts(mGroceryListsModel, mGroceryListProductsModels);
                }
                break;
            default:
                break;
        }
    }

    private void checkProductsQuantity(){
        if(mGroceryListProductsModels != null){
            int size = mGroceryListProductsModels.size();
            for(int i = 0; i < size; i++){
                if(mGroceryListProductsModels.get(i).getQuantity() == 0){
                    mGroceryListProductsModels.remove(i);
                    size --;
                }
            }
        }
    }

    private void hideKeyboard(Button mButton){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mButton.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mRadioButton = mRadioGroup.findViewById(R.id.radioButton);

            if(mRadioButton.isChecked()){
                mAddress.setEnabled(false);
                mTown.setEnabled(false);
                mAddress.setText(CurrentUser.getCurrentUser.getAddress());
                mTown.setText(CurrentUser.getCurrentUser.getTown());
            }
            else{
                mAddress.setEnabled(true);
                mTown.setEnabled(true);
                mAddress.getText().clear();
                mTown.getText().clear();
                mAddress.setHint(getResources().getString(R.string.anotherAddress));
                mTown.setHint(getResources().getString(R.string.anotherCity));

                //PONOVI
                if(mGroceryListsModel != null){
                    mAddress.setText(mGroceryListsModel.getDelivery_address());
                    mTown.setText(mGroceryListsModel.getDelivery_town());
                }
            }
        }
    };


    @Override
    public void storesReceived(ArrayList<StoresModel> mStores) {
        if(mStores != null){
            ArrayList<String> storeNames = new ArrayList<>();
            for(int i = 0; i < mStores.size(); i++){
                storeNames.add(mStores.get(i).getName());
            }

            if(getActivity() != null){
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, storeNames);
                mSpinnerStores.setAdapter(adapter);
            }

            //PONOVI
            if(mRepeat && mGroceryListsModel != null){
                fillDataOnRepeatGL();
            }

        }

    }
    AdapterView.OnItemSelectedListener onItemSelectedListener= new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //Ako je fragment već kreiran, ako su dohvaćeni podaci o proizvodima i ako je dućan jednak odabranom itemu u spinneru
            if(!mFirstEntry && mSended && mSelectedStoreName != null && parent.getItemAtPosition(position) != null && (!mSelectedStoreName.equals(parent.getItemAtPosition(position).toString()))){
                if(mProductsListAdapter.getItemCount() > 0)
                    showDialogOnStoreChanged(true);
                else
                    showDialogOnStoreChanged(false);
                if(mSended){
                    mSpinnerStores.setSelection(mPositionInSpinner);//ako je u dialogbox odabran NE
                    mFlag = 1;
                }
            }
            if(mSended && mFirstEntry){
                mSpinnerStores.setSelection(mPositionInSpinner);
                mFirstEntry = false;
            }
            else{
                if(mFlag == 0){
                    mSelectedStoreName = parent.getItemAtPosition(position).toString();
                    mPositionInSpinner = parent.getSelectedItemPosition();
                }
                else{
                    mFlag = 0;
                }
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    };


    @Override
    public void productsListReceived(List<GroceryListProductsModel> mProductsList) {
        if(mProductsList != null){
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            mProductsListAdapter = new ProductsListAdapter(mProductsList, mTotalPriceAmount);
            mRecyclerView.setAdapter(mProductsListAdapter);
        }
    }

    @Override
    public void groceryListAddedToDatabase(boolean mSuccess, String mMessage) {
        showToast(mMessage);

        if(mSuccess && getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
    }


    public boolean checkData(){

        boolean entered = true;
        if(isNullOrBlank(mSelectedStoreName)){
            showToast(getResources().getString(R.string.chooseStore));
            entered = false;

        }
        if(isNullOrBlank(mAddress.getText().toString())){
            showToast(getResources().getString(R.string.chooseAddress));
            entered = false;
        }
        if(isNullOrBlank(mTown.getText().toString())){
            showToast(getResources().getString(R.string.chooseCity));
            entered = false;
        }
        if(isNullOrBlank(mStartDate.toString())){
            showToast(getResources().getString(R.string.chooseStartDate));
            entered = false;
        }
        if(isNullOrBlank(mCommission.getText().toString())){
            showToast(getResources().getString(R.string.chooseCommission));
            entered = false;
        }
        checkProductsQuantity();//ako je količina 0 briše iz liste
        if(mGroceryListProductsModels != null){
            if(mGroceryListProductsModels.size() < 1){
                showToast(getResources().getString(R.string.atLeastOneProduct));
                entered = false;
            }
        }
        else{
            showToast(getResources().getString(R.string.noProductListError));
            entered = false;
        }


        return  entered;
    }

    private boolean isNullOrBlank(String s)
    {
        return (s == null || s.trim().equals(""));
    }

    public void createGroceryList(){
        mGroceryListsModel= new GroceryListsModel();

        mGroceryListsModel.setCommision(mCommission.getText().toString());
        mGroceryListsModel.setDelivery_address(mAddress.getText().toString());
        mGroceryListsModel.setDelivery_town(mTown.getText().toString());
        mGroceryListsModel.setEnd_date(increaseCurrentDateBy(3));
        mGroceryListsModel.setStart_date(mStartDate.getText().toString());
        mGroceryListsModel.setStatus(GroceryListStatus.CREATED);
        mGroceryListsModel.setStore_name(mSelectedStoreName);
        mGroceryListsModel.setTotal_price(mTotalPriceAmount.getText().toString());
        mGroceryListsModel.setUser_id(CurrentUser.getCurrentUser.getUserUID());
        mGroceryListsModel.setUser_accepted_id("-");
        mGroceryListsModel.setLongitude(CurrentUser.getCurrentUser.getLongitude());
        mGroceryListsModel.setLatitude(CurrentUser.getCurrentUser.getLatitude());
    }


    public String increaseCurrentDateBy(int mValue){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        c.add(Calendar.DATE, mValue);
        String endDate = df.format(c.getTime());
        return endDate;
    }

    public void showToast(String mMessage){
        Toast.makeText(getActivity(), mMessage, Toast.LENGTH_LONG).show();
    }

    public void showDialogOnStoreChanged(boolean mFlag){
        if(mFlag) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.storeChangeTitle))
                    .setMessage(getResources().getString(R.string.storeChangeMessage))
                    .setNegativeButton(getResources().getString(R.string.no), null)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mProductsListAdapter != null && mGroceryListProductsModels != null) {
                                mGroceryListProductsModels.clear();
                                productsListReceived(mGroceryListProductsModels);
                                mSended = false;

                            }

                        }
                    }).create().show();
        }
        else{
            mGroceryListProductsModels.clear();
            productsListReceived(mGroceryListProductsModels);
            mSended = false;
        }
    }

    private String getDate(){
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String todayString = formatter.format(today);
        return todayString;
    }



}
