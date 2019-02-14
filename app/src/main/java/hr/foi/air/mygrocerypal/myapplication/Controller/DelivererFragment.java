package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.NavigationItem;
import hr.foi.air.mygrocerypal.myapplication.R;

/**
 * Fragment koji se otvara prilikom ulazka u aplikaciju i pritiskom na gumb Deliverer u drawerlayotu / izborniku
 */
public class DelivererFragment extends Fragment implements NavigationItem {
    private Button btnAccepted, btnActive, btnIgnored;
    private ArrayList<Fragment> delivererFragments;
    int mFlag;

    /**
     * Pritisak na gumb Active, Accepted, Ignored
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.active_deliverer_btn: //mFlag = 0
                    if(mFlag != 0){
                        mFlag = 0;
                        showGroceryLists(delivererFragments.get(mFlag));
                        changeBtnColor(mFlag);
                    }
                    break;
                case R.id.accepted_deliverer_btn: //mFlag = 1
                    if(mFlag != 1){
                        mFlag = 1;
                        showGroceryLists(delivererFragments.get(mFlag));
                        changeBtnColor(mFlag);
                    }
                    break;
                case R.id.ignored_client_btn: //mFlag = 2
                    if(mFlag != 2){
                        mFlag = 2;
                        showGroceryLists(delivererFragments.get(mFlag));
                        changeBtnColor(mFlag);
                    }
                    break;
                default:
                    Log.d("CLICKDELIVERERFRAGMENT", "PRITISNUT JE: " + Integer.toString(v.getId()));
                    break;
            }
        }
    };

    /**
     * Promijena boje pritisnutog gumba
     * @param flag
     */
    private void changeBtnColor(int flag){
        if(flag == 0){
            btnActive.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            btnAccepted.setBackgroundColor(Color.WHITE);
            btnIgnored.setBackgroundColor(Color.WHITE);
        }
        else if(flag == 1){
            btnAccepted.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            btnActive.setBackgroundColor(Color.WHITE);
            btnIgnored.setBackgroundColor(Color.WHITE);
        }
        else {
            btnIgnored.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            btnActive.setBackgroundColor(Color.WHITE);
            btnAccepted.setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * Nastavljanje izvodenja fragmenta, postavljanje naslova u activityju
     */
    @Override
    public void onResume() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.deliverer));
        super.onResume();
    }

    /**
     * Inicijalizacija
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

        btnActive.setOnClickListener(clickListener);
        btnAccepted.setOnClickListener(clickListener);
        btnIgnored.setOnClickListener(clickListener);

        setUpDelivererFragments();
        showGroceryLists(delivererFragments.get(mFlag));

        Log.d("DelivererFragment", "DelivererFragment");
        return view;
    }

    /**
     * Napuni listu fragmentima za deliverera
     * ActiveDelivererFragment -> aktivni gl-ovi
     * AcceptedDelivererFragment -> prihvaceni gl-ovi
     * IgnoredDelivererFragment -> ignorirani gl-ovi
     */
    private void setUpDelivererFragments(){
        if(delivererFragments != null) {
            changeBtnColor(mFlag);
            return;
        }
        delivererFragments = new ArrayList<>();
        delivererFragments.add(new ActiveDelivererFragment());
        delivererFragments.add(new AcceptedDelivererFragment());
        delivererFragments.add(new IgnoredDelivererFragment());
    }

    /**
     * Prikaz novog fragmenta unutar fragmenta DelivererFragment
     * @param mFragment
     */
    public void showGroceryLists(Fragment mFragment){
        getChildFragmentManager().beginTransaction()
                .replace(R.id.show_grocery_lists, mFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.deliverer_fragment);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public Drawable getIcon(Context context) {
        return context.getDrawable(R.drawable.ic_person_black_24dp);
    }

    /**
     * Dohvati active deliverer fragment
     * @return
     */
    public ActiveDelivererFragment getActiveDelivererFragment(){
        ActiveDelivererFragment fragment = null;
        for(Fragment frag : delivererFragments){
            if(frag instanceof ActiveDelivererFragment){
                fragment = (ActiveDelivererFragment) frag;
                break;
            }
        }
        return fragment;
    }
}
