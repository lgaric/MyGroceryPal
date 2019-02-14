package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.NavigationItem;
import hr.foi.air.mygrocerypal.myapplication.R;
import hr.foi.air.mygrocerypal.myapplication.SecondNavigationItem;
import hr.foi.air.mygrocerypal.myapplication.TopNavigation;

/**
 * Fragment koji se otvara prilikom ulazka u aplikaciju i pritiskom na gumb Deliverer u drawerlayotu / izborniku
 */
public class DelivererFragment extends Fragment implements NavigationItem, TopNavigation {
    private ImageButton previousBtn, nextBtn;
    private TextView currentFragmentName;
    private ArrayList<SecondNavigationItem> delivererFragments;
    private int currentArrayIndex = 0;

    /**
     * Pritisak na gumb Active, Accepted, Ignored
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.previous_fragment:
                    if(currentArrayIndex == 0)
                        currentArrayIndex = delivererFragments.size() - 1;
                    else
                        currentArrayIndex--;
                    changeFragment(delivererFragments.get(currentArrayIndex).getFragment());
                    break;
                case R.id.next_fragment:
                    if(currentArrayIndex == delivererFragments.size() - 1)
                        currentArrayIndex = 0;
                    else
                        currentArrayIndex++;
                    changeFragment(delivererFragments.get(currentArrayIndex).getFragment());
                    break;
                default:
                    Log.d("CLICKDELIVERERFRAGMENT", "PRITISNUT JE: " + Integer.toString(v.getId()));
                    break;
            }
        }
    };

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

        previousBtn = view.findViewById(R.id.previous_fragment);
        nextBtn = view.findViewById(R.id.next_fragment);
        currentFragmentName = view.findViewById(R.id.current_fragment_name);
        currentFragmentName.setGravity(Gravity.CENTER);

        previousBtn.setOnClickListener(clickListener);
        nextBtn.setOnClickListener(clickListener);

        setUpDelivererFragments();
        changeFragment(delivererFragments.get(currentArrayIndex).getFragment());

        return view;
    }

    /**
     * Napuni listu fragmentima za deliverera
     * ActiveDelivererFragment -> aktivni gl-ovi
     * AcceptedDelivererFragment -> prihvaceni gl-ovi
     * IgnoredDelivererFragment -> ignorirani gl-ovi
     */
    private void setUpDelivererFragments(){
        if(delivererFragments != null)
            return;
        delivererFragments = new ArrayList<>();
        delivererFragments.add(new ActiveDelivererFragment());
        delivererFragments.add(new AcceptedDelivererFragment());
        delivererFragments.add(new IgnoredDelivererFragment());
        delivererFragments.add(new FinishedDelivererFragment());
    }

    public ActiveDelivererFragment getActiveDelivererFragment(){
        ActiveDelivererFragment fragment = null;
        for(SecondNavigationItem frag : delivererFragments){
            if(frag instanceof ActiveDelivererFragment){
                fragment = (ActiveDelivererFragment) frag;
                break;
            }
        }
        return fragment;
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

    @Override
    public void changeFragment(Fragment item) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.show_grocery_lists, item)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        changeNavigationName(delivererFragments.get(currentArrayIndex).getName(getContext()));
    }

    @Override
    public void changeNavigationName(String name) {
        currentFragmentName.setText(name);
    }
}
