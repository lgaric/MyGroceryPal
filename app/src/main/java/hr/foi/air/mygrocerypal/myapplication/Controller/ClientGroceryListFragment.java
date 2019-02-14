package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import hr.foi.air.mygrocerypal.myapplication.NavigationItem;
import hr.foi.air.mygrocerypal.myapplication.R;

public class ClientGroceryListFragment extends Fragment implements View.OnClickListener, GroceryListListener,
        GroceryListClickListener, NavigationItem {

    private GroceryListHelper mPastGroceryListHelper;
    private Button btnActiveGroceryList, btnPastGroceryList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFloatingButtonAdd;
    private RecyclerView mRecyclerView;
    private GroceryListAdapter mGroceryListAdapter;
    private TextView mNoneClientGroceryLists;
    int mFlag = 0;

    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            showGroceryLists();
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
        View view = inflater.inflate(R.layout.fragment_client_grocerylist, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.client));

        btnActiveGroceryList = view.findViewById(R.id.active_client_btn);
        btnPastGroceryList = view.findViewById(R.id.past_client_btn);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefreshPastLists);
        mFloatingButtonAdd = view.findViewById(R.id.floatingButtonAdd);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mNoneClientGroceryLists = view.findViewById(R.id.noneClientGL);

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);

        btnActiveGroceryList.setOnClickListener(this);
        btnPastGroceryList.setOnClickListener(this);
        mFloatingButtonAdd.setOnClickListener(this);

        Log.d("ClientGroceryListFragme", "ClientGroceryListFragment");
        return view;
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
     * Prikazivanje GL-ova
     */
    private void showGroceryLists(){
        if(mFlag == 0)
            mPastGroceryListHelper.loadGroceryLists(GroceryListStatus.ACCEPTED, CurrentUser.getCurrentUser.getUserUID());
        else
            mPastGroceryListHelper.loadGroceryLists(GroceryListStatus.FINISHED, CurrentUser.getCurrentUser.getUserUID());
    }

    /**
     * Kreiranje view-a fragmenta
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mPastGroceryListHelper == null)
            mPastGroceryListHelper = new GroceryListHelper(this);
        setBtnColor();
        showGroceryLists();
    }

    /**
     * Metoda usmjeravanja pritiska na ekran
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.active_client_btn:
                if(mFlag != 0) {
                    mFlag = 0;
                    setBtnColor();
                    showGroceryLists();
                }
                break;
            case R.id.past_client_btn:
                if(mFlag != 1){
                    mFlag = 1;
                    setBtnColor();
                    showGroceryLists();
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

    private void setBtnColor(){
        if(mFlag == 0){
            btnActiveGroceryList.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            btnPastGroceryList.setBackgroundColor(Color.WHITE);
        }
        else {
            btnPastGroceryList.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            btnActiveGroceryList.setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * Upisivanje detalja fragmenta
     * @param mGroceryListsModel
     */
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

    /**
     * Dobivanje Grocery Listi
     * @param mGroceryList
     * @param mGroceryListStatus
     */
    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null){
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mGroceryListAdapter = new GroceryListAdapter(mGroceryList, this);
            mRecyclerView.setAdapter(mGroceryListAdapter);
            mSwipeRefreshLayout.setRefreshing(false);
            setTextVisibility(mGroceryList, mGroceryListStatus);
        }else if (mGroceryListStatus.equals(GroceryListStatus.ACCEPTED)){
            mNoneClientGroceryLists.setText(getResources().getString(R.string.clientActiveGLMessage));
            mNoneClientGroceryLists.setVisibility(View.VISIBLE);
        }else{
            mNoneClientGroceryLists.setText(getResources().getString(R.string.clientPastGLMessage));
            mNoneClientGroceryLists.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Odabir pojedine grocery liste
     * @param mGroceryListsModel
     */
    @Override
    public void groceryListSelected(GroceryListsModel mGroceryListsModel) {
        loadFragmentDetails(mGroceryListsModel);
    }

    /**
     * Postavljanje vidljivosti grocery lista
     * @param mGroceryList
     * @param mGroceryListStatus
     */
    private void setTextVisibility(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus){
        if(mGroceryList.size() == 0 && mGroceryListStatus.equals(GroceryListStatus.ACCEPTED)){
            mNoneClientGroceryLists.setText(getResources().getString(R.string.clientActiveGLMessage));
            mNoneClientGroceryLists.setVisibility(View.VISIBLE);
        }else if (mGroceryList.size() == 0 && mGroceryListStatus.equals(GroceryListStatus.FINISHED)){
            mNoneClientGroceryLists.setText(getResources().getString(R.string.clientPastGLMessage));
            mNoneClientGroceryLists.setVisibility(View.VISIBLE);
        }else
            mNoneClientGroceryLists.setVisibility(View.GONE);

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
}
