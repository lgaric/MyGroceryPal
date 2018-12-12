package hr.foi.air.mygrocerypal.myapplication.View;


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

    Button accepted;
    Button active;
    Button ignored;

    FrameLayout groceryLists;

    int flag = 1;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.active_deliverer_btn: //flag = 1
                    if(flag != 1){
                        flag = 1;
                        showGroceryLists(new ActiveDelivererFragment());
                    }
                    break;
                case R.id.accepted_deliverer_btn: //flag = 2
                    break;
                case R.id.ignored_client_btn: //flag = 3
                    if(flag != 3){
                        flag = 3;
                        showGroceryLists(new IgnoredDelivererFragment());
                    }
                    break;
                default:
                    Log.d("CLICKDELIVERERFRAGMENT", "PRITISNUT JE: " + Integer.toString(v.getId()));
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliverer, container, false);

        active = view.findViewById(R.id.active_deliverer_btn);
        accepted = view.findViewById(R.id.accepted_deliverer_btn);
        ignored = view.findViewById(R.id.ignored_client_btn);
        groceryLists = view.findViewById(R.id.show_grocery_lists);

        active.setOnClickListener(clickListener);
        accepted.setOnClickListener(clickListener);
        ignored.setOnClickListener(clickListener);

        showGroceryLists(new ActiveDelivererFragment());

        return view;
    }

    public void showGroceryLists(Fragment fragment){
        getChildFragmentManager().beginTransaction()
            .replace(R.id.show_grocery_lists, fragment)
            .commit();
    }

}
