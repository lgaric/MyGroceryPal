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

        //TESTIRANJE
        //loadFragment("PastGroceryListFragment");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.active_client_btn:
                //OVDJE PROSLIJEDIS IME SVOJEG FRAGMENTA ZA AKTUALNE GROCERYLISTE
                loadFragment("");
                break;
            case R.id.past_client_btn:
                loadFragment("PastGroceryListFragment");
                break;
        }

    }

    private void loadFragment(String className){
        FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();

        if(className == "PastGroceryListFragment")
            mFragmentTransaction.replace(R.id.show_grocery, new PastGroceryListFragment());
        //else
        //mFragmentTransaction.replace(R.id.grocerylist_clientContainer, VECINA KLASA ZA AKTUALNE GROCERY LISTE);

        mFragmentTransaction.commit();
    }
}
