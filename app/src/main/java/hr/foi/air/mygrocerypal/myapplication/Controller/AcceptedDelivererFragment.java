package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.DelivererGLAdapter;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.DelivererGroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.SecondNavigationItem;

public class AcceptedDelivererFragment extends Fragment implements GroceryListOperationListener, GroceryListStatusListener
    , SecondNavigationItem {
    private TextView mNoneAcceptedLists;

    //vars
    DelivererGroceryListHelper mDelivererGroceryListHelper;
    ArrayList<GroceryListsModel> mAcceptedGroceryList;

    //widgets
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshRecyclerView();
        }
    };

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
        View view = inflater.inflate(R.layout.fragment_deliverer_accepted, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_accepted);
        mRecyclerView = view.findViewById(R.id.grocery_lists_accepted);
        mNoneAcceptedLists = view.findViewById(R.id.noneDelivererAcceptedGL);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);

        if(mDelivererGroceryListHelper == null){
            mDelivererGroceryListHelper = new DelivererGroceryListHelper(this);
            mDelivererGroceryListHelper.getAllAcceptedListsByCurrentUser();
        }
        else
            mDelivererGroceryListHelper.getAllAcceptedListsByCurrentUser();

        return view;
    }

    /**
     * Metoda koja se poziva klikom na pojedini Grocery List
     * @param mGroceryListsModel
     * @param mOperation
     */
    @Override
    public void buttonPressedOnGroceryList(GroceryListsModel mGroceryListsModel, GroceryListOperation mOperation) {
        switch(mOperation){
            case DETAILS:
                showGroceryListDetails(mGroceryListsModel);
                break;
            default:
                break;
        }
    }

    /**
     * Override metode iz listenera za dobivene statuse GL-ova
     * @param mGroceryListID
     * @param mGroceryListStatus
     * @param mOperation
     */
    @Override
    public void groceryListStatusReceived(String mGroceryListID, String mGroceryListStatus, GroceryListOperation mOperation) {

    }

    /**
     * Override metode iz listenera za dobivene GL-ove
     * @param mGroceryList
     * @param mGroceryListStatus
     */
    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null) {
            mAcceptedGroceryList = mGroceryList;
            mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerView.setAdapter(null);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DelivererGLAdapter adapter = new DelivererGLAdapter(mGroceryList, this, 2);
            mRecyclerView.setAdapter(adapter);
            setTextVisibility(mGroceryList);
        }else
            mNoneAcceptedLists.setVisibility(View.VISIBLE);
    }

    /**
     * Prikaži detalje groceryliste
     * @param mGroceryListsModel
     */
    private void showGroceryListDetails(GroceryListsModel mGroceryListsModel){
        Bundle bundle = new Bundle();
        bundle.putSerializable("GROCERY_LIST_MODEL", mGroceryListsModel);
        bundle.putBoolean("IS_DELIVERER", true);
        ShowGroceryListDetailsFragment fragment = new ShowGroceryListDetailsFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Pullldown za refreshanje grocery lista
     */
    private void refreshRecyclerView(){
        if(mDelivererGroceryListHelper != null)
            mDelivererGroceryListHelper.getAllAcceptedListsByCurrentUser();
    }

    /**
     * Postavljanje vidljivosti grocery lista
     * @param mGroceryList
     */
    private void setTextVisibility(ArrayList<GroceryListsModel> mGroceryList){
        if(mGroceryList.size() > 0)
            mNoneAcceptedLists.setVisibility(View.GONE);
        else
            mNoneAcceptedLists.setVisibility(View.VISIBLE);
    }

    @Override
    public String getName(Context context) {
        return context.getResources().getString(R.string.acceptedCaps);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
