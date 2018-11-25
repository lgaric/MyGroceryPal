package hr.foi.air.mygrocerypal.myapplication.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ClientGroceryListFragment extends Fragment implements View.OnClickListener {

    private Button activeGrocerylistBtn, pastGroceryListBtn;

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
                loadFragment(GroceryListStatus.ACCEPTED);
                break;
            case R.id.past_client_btn:
                loadFragment(GroceryListStatus.FINISHED);
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

    public void showGroceryListDetails(GroceryListsModel groceryListsModel){
        Toast.makeText(getActivity(), groceryListsModel.getGrocerylist_key(), Toast.LENGTH_LONG).show();
    }
}
