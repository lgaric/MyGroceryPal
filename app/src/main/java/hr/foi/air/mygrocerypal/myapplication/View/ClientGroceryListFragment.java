package hr.foi.air.mygrocerypal.myapplication.View;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.collection.LLRBNode;

import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ClientGroceryListFragment extends Fragment implements View.OnClickListener {

    private Button activeGrocerylistBtn, pastGroceryListBtn;
    /*
    0 -> AKTUALNI GROCERYLISTS
    1 -> PROŠLI GROCERYLISTS
     */
    int flag = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_grocerylist, container, false);

        activeGrocerylistBtn = view.findViewById(R.id.active_client_btn);
        pastGroceryListBtn = view.findViewById(R.id.past_client_btn);

        activeGrocerylistBtn.setOnClickListener(this);
        pastGroceryListBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFragment(GroceryListStatus.ACCEPTED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.active_client_btn:
                if(flag != 0) {
                    flag = 0;
                    loadFragment(GroceryListStatus.ACCEPTED);
                }
                break;
            case R.id.past_client_btn:
                if(flag != 1){
                    flag = 1;
                    loadFragment(GroceryListStatus.FINISHED);
                }
                break;
        }
    }

    private void loadFragment(GroceryListStatus status){
        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
        ShowGroceryListFragment fragment = new ShowGroceryListFragment();
        fragment.setArguments(getBundle(status));
        mFragmentTransaction.replace(R.id.show_grocery, fragment);
        mFragmentTransaction.commit();
    }

    private Bundle getBundle(GroceryListStatus status){
        Bundle bundle = new Bundle();

        if(status == GroceryListStatus.FINISHED)
            bundle.putBoolean("ACTIVE", false);
        else
            bundle.putBoolean("ACTIVE", true);

        return bundle;
    }

    private void loadFragmentDetails(GroceryListsModel groceryListsModel){
        Bundle bundle = new Bundle();
        bundle.putSerializable("GROCERY_LIST_MODEL", groceryListsModel);
        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
        ShowGroceryListDetailsFragment fragment = new ShowGroceryListDetailsFragment();
        fragment.setArguments(bundle);
        mFragmentTransaction.replace(R.id.show_grocery, fragment);
        mFragmentTransaction.commit();
    }

    public void showGroceryListDetails(GroceryListsModel groceryListsModel){
        flag = -1;
        loadFragmentDetails(groceryListsModel);
    }

    public void refresh(boolean past){
        if(!past)
            loadFragment(GroceryListStatus.FINISHED);
        else
            loadFragment(GroceryListStatus.ACCEPTED);
    }

}
