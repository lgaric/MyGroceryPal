package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.GroceryListAdapter;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.GroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListClickListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ClientGroceryListFragment extends Fragment implements View.OnClickListener, GroceryListListener, GroceryListClickListener {

    private GroceryListHelper mPastGroceryListHelper;
    private Button btnActiveGrocerylist, btnPastGroceryList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFloatingButtonAdd;
    private RecyclerView mRecyclerView;
    private GroceryListAdapter mGroceryListAdapter;
    private TextView noneClientGroceryLists;
    /*
    0 -> AKTUALNI GROCERYLISTS
    1 -> PROŠLI GROCERYLISTS
     */
    boolean mActive;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            showGroceryLists();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_grocerylist, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Client");

        btnActiveGrocerylist = view.findViewById(R.id.active_client_btn);
        btnPastGroceryList = view.findViewById(R.id.past_client_btn);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefreshPastLists);
        mFloatingButtonAdd = view.findViewById(R.id.floatingButtonAdd);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        noneClientGroceryLists = view.findViewById(R.id.noneClientGL);

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);

        btnActiveGrocerylist.setOnClickListener(this);
        btnPastGroceryList.setOnClickListener(this);
        mFloatingButtonAdd.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Client");
        super.onResume();
    }

    private void showGroceryLists(){
        if(mActive)
            mPastGroceryListHelper.loadGroceryLists(GroceryListStatus.ACCEPTED, CurrentUser.getCurrentUser.getUserUID());
        else
            mPastGroceryListHelper.loadGroceryLists(GroceryListStatus.FINISHED, CurrentUser.getCurrentUser.getUserUID());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mPastGroceryListHelper = new GroceryListHelper(this);
        super.onViewCreated(view, savedInstanceState);
        mActive = true;
        loadGroceryListToRecyclerView(GroceryListStatus.ACCEPTED);
        btnActiveGrocerylist.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.active_client_btn:
                //AKO NISU PRIKAZANI AKTIVNI GL ONDA IH PRIKAZI
                if(!mActive) {
                    loadGroceryListToRecyclerView(GroceryListStatus.ACCEPTED);
                    btnActiveGrocerylist.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    btnPastGroceryList.setBackgroundColor(Color.WHITE);
                    mActive = true;
                }
                break;
            case R.id.past_client_btn:
                if(mActive){
                    loadGroceryListToRecyclerView(GroceryListStatus.FINISHED);
                    btnPastGroceryList.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    btnActiveGrocerylist.setBackgroundColor(Color.WHITE);
                    mActive = false;
                }
                break;
            case R.id.floatingButtonAdd:
                CreateNewGroceryListFragment createNewGroceryListFragment = new CreateNewGroceryListFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, createNewGroceryListFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                break;
        }
    }

    private void loadGroceryListToRecyclerView(GroceryListStatus mStatus){
        mPastGroceryListHelper.loadGroceryLists(mStatus, CurrentUser.getCurrentUser.getUserUID());
    }


    //NEZANIMA NAS
    private void loadFragmentDetails(GroceryListsModel mGroceryListsModel){
        Bundle bundle = new Bundle();
        bundle.putSerializable("GROCERY_LIST_MODEL", mGroceryListsModel);
        ShowGroceryListDetailsFragment fragment = new ShowGroceryListDetailsFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null){
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mGroceryListAdapter = new GroceryListAdapter(mGroceryList, this);
            mRecyclerView.setAdapter(mGroceryListAdapter);

            //MAKNI OZNAKU ZA OSVJEZAVANJE
            mSwipeRefreshLayout.setRefreshing(false);
            setTextVisibility(mGroceryList, mGroceryListStatus);
        }else if (mGroceryListStatus.equals(GroceryListStatus.ACCEPTED)){
            noneClientGroceryLists.setText("Trenutno nemate aktivne kupovne liste.");
            noneClientGroceryLists.setVisibility(View.VISIBLE);
        }else{
            noneClientGroceryLists.setText("Trenutno nemate prošle kupovne liste.");
            noneClientGroceryLists.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void groceryListSelected(GroceryListsModel mGroceryListsModel) {
        loadFragmentDetails(mGroceryListsModel);
    }

    private void setTextVisibility(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus){
        if(mGroceryList.size() == 0 && mGroceryListStatus.equals(GroceryListStatus.ACCEPTED)){
            noneClientGroceryLists.setText("Trenutno nemate aktivne kupovne liste.");
            noneClientGroceryLists.setVisibility(View.VISIBLE);
        }else if (mGroceryList.size() == 0 && mGroceryListStatus.equals(GroceryListStatus.FINISHED)){
            noneClientGroceryLists.setText("Trenutno nemate prošle kupovne liste.");
            noneClientGroceryLists.setVisibility(View.VISIBLE);
        }else
            noneClientGroceryLists.setVisibility(View.GONE);

    }
}
