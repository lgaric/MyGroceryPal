package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.GroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.NavigationItem;
import hr.foi.air.mygrocerypal.myapplication.R;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class StatisticsFragment extends Fragment implements GroceryListListener, NavigationItem {

    PieChartView mPieChartView;
    List<SliceValue> mPieData;
    TextView mTotalOrderPriceWithProvision, mTotalOrderCommission, mNumberOfOrders, mAverageOrderPrice, mTotalOrderPriceWithoutCommission;
    TextView mNumberOfDeliveries, mAverageDeliveryPrice, mTotalDeliveryCommission, mAverageDeliveryCommission, mTotalDeliveryPrice;
    GroceryListHelper mPastGroceryListHelper;

    /**
     * Konstruktor
     */
    public StatisticsFragment(){ }

    /**
     * OnCreateView metoda fragmenta
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        BindFragmentData(view);
        mPieData = new ArrayList<>();
        mPastGroceryListHelper = new GroceryListHelper(this);
        mPastGroceryListHelper.loadGroceryListsByGroceryListStatus(GroceryListStatus.FINISHED);
        Log.d("StatisticsFragment", "StatisticsFragment");
        return view;
    }

    /**
     * Nastavljanje izvodenja fragmenta
     */
    @Override
    public void onResume() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.statistics));
        super.onResume();
    }

    /**
     * Metoda povezivanja objekata s XML-om
     * @param view
     */
    private void BindFragmentData(View view) {
        mPieChartView = view.findViewById(R.id.statistics_pie_chart);

        mTotalOrderPriceWithProvision = view.findViewById(R.id.totalOrderPriceWithProvision);
        mTotalOrderCommission = view.findViewById(R.id.totalOrderCommissionPrice);
        mNumberOfOrders = view.findViewById(R.id.numberOfOrders);
        mAverageOrderPrice = view.findViewById(R.id.averageOrderPrice);
        mTotalOrderPriceWithoutCommission = view.findViewById(R.id.totalOrderPriceWithoutCommission);

        mNumberOfDeliveries = view.findViewById(R.id.numberOfDeliveries);
        mTotalDeliveryPrice = view.findViewById(R.id.totalDeliveryPrice);
        mAverageDeliveryPrice  = view.findViewById(R.id.averageDeliveryPrice);
        mTotalDeliveryCommission = view.findViewById(R.id.totalDeliveryCommission);
        mAverageDeliveryCommission = view.findViewById(R.id.averageDeliveryCommission);
    }

    /**
     * Metoda u kojoj se naruceni GL-ovi ispisuju, zajedno sa statistikom
     * @param mGroceryList
     * @param totalOrderPriceWithProvision
     * @param totalCommissionPrice
     * @param numberOfOrders
     */
    public void GroceryListOrders(ArrayList<GroceryListsModel> mGroceryList, float totalOrderPriceWithProvision,
                                  float totalCommissionPrice, int numberOfOrders) {
        if(mGroceryList == null) {
            return;
        }
        mPieData.add(new SliceValue(totalOrderPriceWithProvision, Color.MAGENTA).setLabel(getResources().getString(R.string.ordering) + Math.round(totalOrderPriceWithProvision)));
        mNumberOfOrders.setText(String.valueOf(numberOfOrders));
        mTotalOrderPriceWithProvision.setText(String.valueOf(Math.round(totalOrderPriceWithProvision) + getResources().getString(R.string.currency)));
        mTotalOrderCommission.setText(String.valueOf(Math.round(totalCommissionPrice) + getResources().getString(R.string.currency)));
        mTotalOrderPriceWithoutCommission.setText(String.valueOf(Math.round(totalOrderPriceWithProvision-totalCommissionPrice) + getResources().getString(R.string.currency)));
        mAverageOrderPrice.setText(String.valueOf(Math.round(totalOrderPriceWithProvision/numberOfOrders) + getResources().getString(R.string.currency)));
        printOutGraph();
    }

    /**
     * Metoda u kojoj se dostavljeni GL-ovi ispisuju, zajedno sa statistikom
     * @param mGroceryList
     * @param totalDeliveryPrice
     * @param numberOfDeliveries
     * @param totalCommission
     */
    private void GroceryListDeliveries(ArrayList<GroceryListsModel> mGroceryList, float totalDeliveryPrice,
                                       int numberOfDeliveries, float totalCommission) {
        if(mGroceryList == null) {
            return;
        }
        mPieData.add(new SliceValue(totalDeliveryPrice, Color.CYAN).setLabel(getResources().getString(R.string.delivering) + Math.round(totalDeliveryPrice)));
        mNumberOfDeliveries.setText(String.valueOf(numberOfDeliveries));
        mTotalDeliveryPrice.setText(String.valueOf(Math.round(totalDeliveryPrice) + getResources().getString(R.string.currency)));
        mTotalDeliveryCommission.setText(String.valueOf(Math.round(totalCommission) + getResources().getString(R.string.currency)));
        mAverageDeliveryPrice.setText(String.valueOf(Math.round(totalDeliveryPrice/numberOfDeliveries) + getResources().getString(R.string.currency)));
        mAverageDeliveryCommission.setText(String.valueOf(Math.round(totalCommission/numberOfDeliveries) + getResources().getString(R.string.currency)));

        printOutGraph();
    }


    /**
     * Metoda za ispisivanje grafa
     */
    private void printOutGraph() {
        PieChartData pieChartData = new PieChartData(mPieData);
        pieChartData.setHasLabels(true);
        mPieChartView.setPieChartData(pieChartData);
        mPieChartView.setVisibility(View.VISIBLE);
    }

    /**
     * Implementacija metode GroceryListListener-a u kojoj se svi GL-ovi dijele na narucene i dostavljene na temelju ID-a korisnika
     * @param mGroceryList
     * @param mGroceryListStatus
     */
    @Override
    public void groceryListReceived(ArrayList<GroceryListsModel> mGroceryList, GroceryListStatus mGroceryListStatus) {
        if(mGroceryList != null) {
            ArrayList<GroceryListsModel> ordersList = new ArrayList<>();
            ArrayList<GroceryListsModel> deliveriesList = new ArrayList<>();

            float totalDeliveryPrice = 0, totalCommission = 0, totalOrderPriceWithProvision = 0, totalCommissionPrice= 0;
            int numberOfDeliveries = 0, numberOfOrders = 0;

            for (GroceryListsModel model: mGroceryList) {
                if(model.getUser_id().equals(CurrentUser.getCurrentUser.getUserUID())) {
                    totalOrderPriceWithProvision += Float.parseFloat(model.getTotal_price());
                    totalCommissionPrice += Float.parseFloat(model.getCommision());
                    numberOfOrders++;
                    ordersList.add(model);
                }else if(model.getUser_accepted_id().equals(CurrentUser.getCurrentUser.getUserUID())) {
                    totalDeliveryPrice += Float.parseFloat(model.getTotal_price());
                    totalCommission += Float.parseFloat(model.getCommision());
                    numberOfDeliveries++;
                    deliveriesList.add(model);
                }
            }
            GroceryListOrders(ordersList, totalOrderPriceWithProvision, totalCommissionPrice, numberOfOrders);
            GroceryListDeliveries(deliveriesList, totalDeliveryPrice, numberOfDeliveries, totalCommission);
        }
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.statistics_fragment);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public Drawable getIcon(Context context) {
        return context.getDrawable(R.drawable.ic_trending_up_black_24dp);
    }
}
