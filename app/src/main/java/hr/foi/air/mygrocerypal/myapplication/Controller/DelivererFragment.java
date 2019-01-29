package hr.foi.air.mygrocerypal.myapplication.Controller;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import hr.foi.air.mygrocerypal.myapplication.R;

public class DelivererFragment extends Fragment {

    Button btnAccepted, btnActive, btnIgnored;
    FrameLayout mGroceryLists;
    ActiveDelivererFragment mActiveDelivererFragment;

    int mFlag = 1;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.active_deliverer_btn: //mFlag = 1
                    if(mFlag != 1){
                        mFlag = 1;
                        showGroceryLists(mActiveDelivererFragment);
                        btnActive.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        btnAccepted.setBackgroundColor(Color.WHITE);
                        btnIgnored.setBackgroundColor(Color.WHITE);
                    }
                    break;
                case R.id.accepted_deliverer_btn: //mFlag = 2
                    if(mFlag != 2){
                        mFlag = 2;
                        showGroceryLists(new AcceptedDelivererFragment());
                        btnAccepted.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        btnActive.setBackgroundColor(Color.WHITE);
                        btnIgnored.setBackgroundColor(Color.WHITE);
                    }
                    break;
                case R.id.ignored_client_btn: //mFlag = 3
                    if(mFlag != 3){
                        mFlag = 3;
                        showGroceryLists(new IgnoredDelivererFragment());
                        btnIgnored.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                        btnActive.setBackgroundColor(Color.WHITE);
                        btnAccepted.setBackgroundColor(Color.WHITE);
                    }
                    break;
                default:
                    Log.d("CLICKDELIVERERFRAGMENT", "PRITISNUT JE: " + Integer.toString(v.getId()));
                    break;
            }
        }
    };

    /**
     * Nastavljanje izvodenja fragmenta
     */
    @Override
    public void onResume() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.deliverer));
        super.onResume();
    }


    /**
     * Kreiranje viewa fragemnta
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.deliverer));

        btnActive = view.findViewById(R.id.active_deliverer_btn);
        btnAccepted = view.findViewById(R.id.accepted_deliverer_btn);
        btnIgnored = view.findViewById(R.id.ignored_client_btn);
        mGroceryLists = view.findViewById(R.id.show_grocery_lists);

        btnActive.setOnClickListener(clickListener);
        btnAccepted.setOnClickListener(clickListener);
        btnIgnored.setOnClickListener(clickListener);


        if(mActiveDelivererFragment == null)
            mActiveDelivererFragment = new ActiveDelivererFragment();
        showGroceryLists(mActiveDelivererFragment);

        return view;
    }

    /**
     * Prikaz GL-ova
     * @param mFragment
     */
    public void showGroceryLists(Fragment mFragment){
        getChildFragmentManager().beginTransaction()
                .replace(R.id.show_grocery_lists, mFragment)
                .commit();
    }

}
