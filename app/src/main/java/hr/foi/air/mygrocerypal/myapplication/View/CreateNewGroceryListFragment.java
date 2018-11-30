package hr.foi.air.mygrocerypal.myapplication.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Controller.CreateNewGroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.GroceryListController;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.StoresListener;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class CreateNewGroceryListFragment extends Fragment implements StoresListener {

    private CreateNewGroceryListController createNewGroceryListController;
    private Spinner spinnerStores;
    //private static final String[] stores = {"Konzum", "Lidl", "Kaufland"};
    private  ArrayList<StoresModel> storesArray;
    private String selectedStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_grocerylist, container, false);

        TextView labelForStore = view.findViewById(R.id.labelForStore);

        spinnerStores = view.findViewById(R.id.spinnerStores);


        return view;
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
            storesArray = stores;
            ArrayList<String> storeNames = new ArrayList<>();
            for(int i = 0; i < stores.size(); i++){
                storeNames.add(stores.get(i).getName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, storeNames);
            spinnerStores.setAdapter(adapter);
            spinnerStores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedStore = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Odabrani dućan: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
