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

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.GroceryListAdapter;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListClickListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.GroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.SecondNavigationItem;

public class ActiveClientFragment extends Fragment implements SecondNavigationItem, GroceryListListener
    , GroceryListClickListener {
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecycleView;
    private TextView mMessage;
    private GroceryListHelper mPastGroceryListHelper;
    private ArrayList<GroceryListsModel> mGroceryList;

    //TODO
    //Filtrirati da lista sadrzi samo GL-ove novije od 2 dana

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_active_finished_expired, container, false);
        mSwipeRefresh = view.findViewById(R.id.swiperefreshPastLists);
        mRecycleView = view.findViewById(R.id.recycler_view);
        mMessage = view.findViewById(R.id.noneClientGL);
        mSwipeRefresh.setOnRefreshListener(mRefreshListener);
        mRecycleView.setVisibility(View.GONE);
        if(mPastGroceryListHelper == null) {
            mPastGroceryListHelper = new GroceryListHelper(this);
            mPastGroceryListHelper.loadGroceryLists(GroceryListStatus.ACCEPTED, CurrentUser.getCurrentUser.getUserUID());
        }
        else
            setRecyclerView();
        return view;
    }

    private void setTextVisibility(){
        if(mGroceryList.size() == 0)
            mMessage.setVisibility(View.VISIBLE);
        else
            mMessage.setVisibility(View.GONE);
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.expiredCaps);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null){
            this.mGroceryList = mGroceryList;
            setRecyclerView();
        }
    }

    public void setRecyclerView(){
        if(mGroceryList != null){
            mRecycleView.setVisibility(View.VISIBLE);
            mRecycleView.setAdapter(null);
            mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
            GroceryListAdapter mGroceryListAdapter = new GroceryListAdapter(this.mGroceryList, this);
            mRecycleView.setAdapter(mGroceryListAdapter);
            mSwipeRefresh.setRefreshing(false);
        }
        else
            setTextVisibility();
    }

    @Override
    public void groceryListSelected(GroceryListsModel mGroceryListsModel) {
        if(mGroceryListsModel != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("GROCERY_LIST_MODEL", mGroceryListsModel);
            ShowGroceryListDetailsFragment fragment = new ShowGroceryListDetailsFragment();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mPastGroceryListHelper.loadGroceryLists(GroceryListStatus.ACCEPTED, CurrentUser.getCurrentUser.getUserUID());
        }
    };
}
