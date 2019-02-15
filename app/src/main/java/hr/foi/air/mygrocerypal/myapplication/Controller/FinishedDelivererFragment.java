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
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.SecondNavigationItem;

public class FinishedDelivererFragment extends Fragment implements GroceryListOperationListener,
        GroceryListStatusListener, SecondNavigationItem{
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private TextView message;
    private DelivererGroceryListHelper mDelivererGroceryListHelper;
    private ArrayList<GroceryListsModel> groceryLists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer_finished, container, false);
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.recycler_view);
        message = view.findViewById(R.id.finishedItemsMessage);
        refreshLayout.setOnRefreshListener(mRefreshListener);
        recyclerView.setVisibility(View.INVISIBLE);

        mDelivererGroceryListHelper = new DelivererGroceryListHelper(this);
        mDelivererGroceryListHelper.getUserFinishedGroceryLists();
        return view;
    }

    @Override
    public String getName(Context context) {
        return context.getResources().getString(R.string.finishedCaps);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    private void refreshRecyclerView(){
        if(mDelivererGroceryListHelper != null)
            mDelivererGroceryListHelper.getUserFinishedGroceryLists();
    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshRecyclerView();
        }
    };

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null){
            groceryLists = mGroceryList;
            refreshLayout.setRefreshing(false);
            recyclerView.setAdapter(null);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DelivererGLAdapter adapter = new DelivererGLAdapter(mGroceryList, this,4);
            recyclerView.setAdapter(adapter);
            setTextVisibility(mGroceryList);
        }
    }

    private void setTextVisibility(ArrayList<GroceryListsModel> mGroceryList){
        if(mGroceryList.size() > 0)
            message.setVisibility(View.GONE);
        else
            message.setVisibility(View.VISIBLE);
    }

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

    //DEBELI INTERFEJSI SU MI NAJDRAZI
    @Override
    public void groceryListStatusReceived(String mGroceryListID, String mGroceryListStatus, GroceryListOperation mOperation) {

    }
}
