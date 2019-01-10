package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class AcceptedDelivererFragment extends Fragment implements GroceryListOperationListener, GroceryListStatusListener {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer_accepted, container, false);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_accepted);
        mRecyclerView = view.findViewById(R.id.grocery_lists_accepted);
        mNoneAcceptedLists = view.findViewById(R.id.noneDelivererAcceptedGL);

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);

        if(mDelivererGroceryListHelper == null){
            mDelivererGroceryListHelper = new DelivererGroceryListHelper(this);
            mDelivererGroceryListHelper.getAllAcceptedListsByCurrentUser();
        }
        else
            mDelivererGroceryListHelper.getAllAcceptedListsByCurrentUser();

        return view;
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

    @Override
    public void groceryListStatusReceived(String mGroceryListID, String mGroceryListStatus, GroceryListOperation mOperation) {

    }

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null) {
            mAcceptedGroceryList = mGroceryList;
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DelivererGLAdapter adapter = new DelivererGLAdapter(mGroceryList, this, 2);
            mRecyclerView.setAdapter(adapter);
            mSwipeRefreshLayout.setRefreshing(false);
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
                .addToBackStack(null)
                .commit();
    }

    private void refreshRecyclerView(){
        if(mDelivererGroceryListHelper != null)
            mDelivererGroceryListHelper.getAllAcceptedListsByCurrentUser();
    }

    private void setTextVisibility(ArrayList<GroceryListsModel> mGroceryList){
        if(mGroceryList.size() > 0)
            mNoneAcceptedLists.setVisibility(View.GONE);
        else
            mNoneAcceptedLists.setVisibility(View.VISIBLE);
    }
}
