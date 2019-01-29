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
    private Button btnActiveGroceryList, btnPastGroceryList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFloatingButtonAdd;
    private RecyclerView mRecyclerView;
    private GroceryListAdapter mGroceryListAdapter;
    private TextView mNoneClientGroceryLists;
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

    /**
     * Kreiranje ClientGroceryList fragmenta
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
        if(mActive)
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

        mPastGroceryListHelper = new GroceryListHelper(this);
        super.onViewCreated(view, savedInstanceState);
        mActive = true;
        loadGroceryListToRecyclerView(GroceryListStatus.ACCEPTED);
        btnActiveGroceryList.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
    }

    /**
     * Metoda usmjeravanja pritiska na ekran
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.active_client_btn:
                //AKO NISU PRIKAZANI AKTIVNI GL ONDA IH PRIKAZI
                if(!mActive) {
                    loadGroceryListToRecyclerView(GroceryListStatus.ACCEPTED);
                    btnActiveGroceryList.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    btnPastGroceryList.setBackgroundColor(Color.WHITE);
                    mActive = true;
                }
                break;
            case R.id.past_client_btn:
                if(mActive){
                    loadGroceryListToRecyclerView(GroceryListStatus.FINISHED);
                    btnPastGroceryList.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                    btnActiveGroceryList.setBackgroundColor(Color.WHITE);
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

    /**
     * Meotda za upisivanje GL-ova u RecyclerView
     * @param mStatus
     */
    private void loadGroceryListToRecyclerView(GroceryListStatus mStatus){
        mPastGroceryListHelper.loadGroceryLists(mStatus, CurrentUser.getCurrentUser.getUserUID());
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

            //MAKNI OZNAKU ZA OSVJEZAVANJE
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
}
