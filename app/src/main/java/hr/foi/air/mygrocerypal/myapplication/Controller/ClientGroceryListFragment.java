package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.GroceryListAdapter;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.GroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListClickListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.NavigationItem;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.SecondNavigationItem;
import hr.foi.air.mygrocerypal.myapplication.TopNavigation;

public class ClientGroceryListFragment extends Fragment implements View.OnClickListener, NavigationItem, TopNavigation {
    private ImageButton previousBtn, nextBtn;
    private TextView currentFragmentName;
    private ArrayList<SecondNavigationItem> clientFragments;
    private int currentArrayIndex = 0;
    private FloatingActionButton mFloatingButtonAdd;

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
        View view = inflater.inflate(R.layout.fragment_client_grocerylist, container, false);
        previousBtn = view.findViewById(R.id.previous_fragment);
        nextBtn = view.findViewById(R.id.next_fragment);
        currentFragmentName = view.findViewById(R.id.current_fragment_name);
        currentFragmentName.setGravity(Gravity.CENTER);
        mFloatingButtonAdd = view.findViewById(R.id.floatingButtonAdd);

        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        mFloatingButtonAdd.setOnClickListener(this);

        setUpDelivererFragments();
        changeFragment(clientFragments.get(currentArrayIndex).getFragment());

        return view;
    }

    private void setUpDelivererFragments(){
        if(clientFragments != null)
            return;
        clientFragments = new ArrayList<>();
        clientFragments.add(new ActiveClientFragment());
        clientFragments.add(new ExpiredClientFragment());
        clientFragments.add(new FinishedClientFragment());
    }

    /**
     * Metoda koja se aktivira prilikom povratka u fragment
     */
    @Override
    public void onResume() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.client));
        super.onResume();
    }

    /**
     * Metoda usmjeravanja pritiska na ekran
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previous_fragment:
                if(currentArrayIndex == 0)
                    currentArrayIndex = clientFragments.size() - 1;
                else
                    currentArrayIndex--;
                changeFragment(clientFragments.get(currentArrayIndex).getFragment());
                break;
            case R.id.next_fragment:
                if(currentArrayIndex == clientFragments.size() - 1)
                    currentArrayIndex = 0;
                else
                    currentArrayIndex++;
                changeFragment(clientFragments.get(currentArrayIndex).getFragment());
                break;
            case R.id.floatingButtonAdd:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CreateNewGroceryListFragment())
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                break;
        }
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.client_fragment);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public Drawable getIcon(Context context) {
        return context.getDrawable(R.drawable.ic_add_shopping_cart_black_24dp);
    }

    @Override
    public void changeFragment(Fragment item) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.show_grocery_lists, item)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        changeNavigationName(clientFragments.get(currentArrayIndex).getName(getContext()));
    }

    @Override
    public void changeNavigationName(String name) {
        currentFragmentName.setText(name);
    }
}
