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
import android.widget.Toast;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Adapters.DelivererGLAdapter;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.DelivererGroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.GroceryListOperationListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

    public class IgnoredDelivererFragment extends Fragment implements GroceryListOperationListener, GroceryListStatusListener{

    private TextView mNoneIgnoredLists;
    //vars
    DelivererGroceryListHelper mDelivererGroceryListHelper;
    ArrayList<GroceryListsModel> mIgnoredGroceryList;

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
         * Kreiranje view-a fragmenta
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer_ignored, container, false);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_ignored);
        mRecyclerView = view.findViewById(R.id.grocery_lists_ignored);
        mNoneIgnoredLists = view.findViewById(R.id.noneDelivererIgnoredGL);

        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);

        if(mDelivererGroceryListHelper == null){
            mDelivererGroceryListHelper = new DelivererGroceryListHelper(this);
            mDelivererGroceryListHelper.getAllIgnoredGroceryLists();
        }
        else
            mDelivererGroceryListHelper.getAllIgnoredGroceryLists();

        return view;
    }

    /**
     * Dobivanje GL-ova
     * @param mGroceryList
     * @param mGroceryListStatus
     */
    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null){
            mIgnoredGroceryList = mGroceryList;
            mRecyclerView.setAdapter(null);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            DelivererGLAdapter adapter = new DelivererGLAdapter(mGroceryList, this,1);
            mRecyclerView.setAdapter(adapter);
            mSwipeRefreshLayout.setRefreshing(false);
            setTextVisibility(mGroceryList);
        }else
            mNoneIgnoredLists.setVisibility(View.VISIBLE);
    }

    /**
     * Pritisak na GL
     * @param mGroceryListsModel
     * @param mOperation
     */
    @Override
    public void buttonPressedOnGroceryList(GroceryListsModel mGroceryListsModel, GroceryListOperation mOperation) {
        switch(mOperation){
            case RETURN:
                mDelivererGroceryListHelper.checkGroceryListStatus(mGroceryListsModel.getGrocerylist_key(), mOperation);
                break;
            case DETAILS:
                showGroceryListDetails(mGroceryListsModel);
                break;
            default:
                break;
        }

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

    /**
     * Osvjezi, dohvati novo zapisane GL-ove iz baze
     */
    private void refreshRecyclerView(){
        if(mDelivererGroceryListHelper != null)
            mDelivererGroceryListHelper.getAllIgnoredGroceryLists();
    }

    /**
     * Dobivanje statusa za GL-ove
     * @param mGroceryListID
     * @param mGroceryListStatus
     * @param mOperation
     */
    @Override
    public void groceryListStatusReceived(String mGroceryListID, String mGroceryListStatus, GroceryListOperation mOperation) {
        String message = "";
        if(mGroceryListStatus.equals("")) {
            Toast.makeText(getActivity(), getResources().getString(R.string.getGroceryListError), Toast.LENGTH_SHORT).show();
            return;
        }else if(!GroceryListStatus.valueOf(mGroceryListStatus).equals(GroceryListStatus.CREATED)){
            Toast.makeText(getActivity(), getResources().getString(R.string.groceryListAlreadyAccepted), Toast.LENGTH_SHORT).show();
            CurrentUser.getCurrentUser.getIgnoredLists().remove(mGroceryListID);
            return;
        }else if(mOperation == GroceryListOperation.RETURN)
            message = mDelivererGroceryListHelper.returnGroceryListFromIgnored(mGroceryListID);
        else
            message = getResources().getString(R.string.defaultErrorMessage);

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            refreshRecyclerView();
        }

        /**
         * Postavljanje vidljivosti grocery lista
         * @param mGroceryList
         */
        private void setTextVisibility(ArrayList<GroceryListsModel> mGroceryList){
            if(mGroceryList.size() > 0)
                mNoneIgnoredLists.setVisibility(View.GONE);
            else
                mNoneIgnoredLists.setVisibility(View.VISIBLE);
        }
    }
