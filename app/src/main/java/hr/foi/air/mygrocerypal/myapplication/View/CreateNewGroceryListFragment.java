package hr.foi.air.mygrocerypal.myapplication.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Controller.Adapters.ProductsListAdapter;
import hr.foi.air.mygrocerypal.myapplication.Controller.CreateNewGroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.AddGroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.ProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class CreateNewGroceryListFragment extends Fragment implements AddGroceryListListener {

    private CreateNewGroceryListController createNewGroceryListController;

    private String storeName;
    private GroceryListsModel groceryListsModel;
    private List<GroceryListProductsModel> groceryListProductsModels;
    private ProductsListAdapter productsListAdapter;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    //widgets
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText address, town, commision;
    private TextView startDate, totalPriceAmount;
    private Button btnAddProducts, btnConfirm;
    private Spinner spinnerStores;
    private RecyclerView recyclerView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_new_grocerylist, container, false);

        totalPriceAmount = view.findViewById(R.id.TotalPriceAmount);
        btnAddProducts = view.findViewById(R.id.btnAddProducts);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        commision = view.findViewById(R.id.commision);
        startDate = view.findViewById(R.id.startDate);
        spinnerStores = view.findViewById(R.id.spinnerStores);
        address = view.findViewById(R.id.address);
        town = view.findViewById(R.id.town);
        recyclerView = view.findViewById(R.id.recycler_view_products);
        address.setText(CurrentUser.currentUser.getAddress());
        town.setText(CurrentUser.currentUser.getTown());
        address.setEnabled(false);
        town.setEnabled(false);
        radioGroup = view.findViewById(R.id.rgroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = view.findViewById(checkedId);

                if(radioButton.getText().equals("Moja adresa")){
                    address.setEnabled(false);
                    town.setEnabled(false);
                    address.setText(CurrentUser.currentUser.getAddress());
                    town.setText(CurrentUser.currentUser.getTown());
                }
                else{
                    address.setEnabled(true);
                    town.setEnabled(true);
                    address.getText().clear();
                    town.getText().clear();
                    address.setHint("Upišite drugu adresu");
                    town.setHint("Upišite grad");
                }
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        btnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectProductsFragment selectProductsFragment = new SelectProductsFragment();
                Bundle bundle = new Bundle();

                storeName = spinnerStores.getSelectedItem().toString();
                bundle.putString("store_name", storeName);
                selectProductsFragment.setArguments(bundle);
                selectProductsFragment.setTargetFragment(CreateNewGroceryListFragment.this, 1);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectProductsFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                startDate.setText(date);
            }
        };

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean entered = checkData();
                if(entered){
                    createGroceryList();
                    createNewGroceryListController.saveGL_withProducts(groceryListsModel, groceryListProductsModels);
                }
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1 && resultCode==Activity.RESULT_OK){
            groceryListProductsModels = (List<GroceryListProductsModel>) data.getSerializableExtra("groceryListOfProducts");
            productsListReceived(groceryListProductsModels);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        createNewGroceryListController = new CreateNewGroceryListController(this);
        createNewGroceryListController.getAllStores();


    }


    @Override
    public void storesReceived(ArrayList<StoresModel> stores) {
        if(stores != null){
            ArrayList<String> storeNames = new ArrayList<>();
            for(int i = 0; i < stores.size(); i++){
                storeNames.add(stores.get(i).getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, storeNames);
            spinnerStores.setAdapter(adapter);
            spinnerStores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    /*if(groceryListProductsModels != null && groceryListProductsModels.size() > 0 && storeName != spinnerStores.getSelectedItem().toString()){
                        showDialogOnStoreChanged();
                    }
                    else{
                        selectedStore = parent.getItemAtPosition(position).toString();
                    }*/

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    @Override
    public void productsListReceived(List<GroceryListProductsModel> productsList) {
        if(productsList != null){
            recyclerView.setAdapter(null);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            productsListAdapter = new ProductsListAdapter(productsList);
            recyclerView.setAdapter(productsListAdapter);
        }
    }

    @Override
    public void groceryListAddedToDatabase(boolean success, String message) {
        showToast(message);

        if(success && getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
    }


    public boolean checkData(){

        boolean entered = true;
        if(isNullOrBlank(storeName)){
            showToast("Odaberite dućan!");
            entered = false;

        }
        if(isNullOrBlank(address.getText().toString())){
            showToast("Odaberite adresu!");
            entered = false;
        }
        if(isNullOrBlank(town.getText().toString())){
            showToast("Odaberite grad!");
            entered = false;
        }
        if(isNullOrBlank(startDate.toString())){
            showToast("Odaberite početni datum prikazivanja!");
            entered = false;
        }
        if(isNullOrBlank(commision.getText().toString())){
            showToast("Odaberite proviziju!");
            entered = false;
        }
        if(groceryListProductsModels != null){
            if(groceryListProductsModels.size() < 1){
                showToast("Morate odabrati barem jedan proizvod!");
                entered = false;
            }
        }
        else{
            showToast("Lista proizvoda nije primljena!");
            entered = false;
        }


        return  entered;
    }

    private boolean isNullOrBlank(String s)
    {
        return (s == null || s.trim().equals(""));
    }

    public void createGroceryList(){
        groceryListsModel = new GroceryListsModel(
                commision.getText().toString(),
                address.getText().toString(), town.getText().toString(),
                "-",
                startDate.getText().toString(),
                GroceryListStatus.CREATED,
                storeName,
                totalPriceAmount.getText().toString(),
                CurrentUser.currentUser.getUserUID());


    }

    public void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void showDialogOnStoreChanged(){
        new AlertDialog.Builder(getActivity())
                .setTitle("Promjena dućana")
                .setMessage("Mijenjanje dućana briše listu proizvoda. Jeste li sigurni da želite promijeniti dućan?")
                .setNegativeButton("Ne", null)
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groceryListProductsModels.clear();
                    }
                }).create().show();
    }
}
