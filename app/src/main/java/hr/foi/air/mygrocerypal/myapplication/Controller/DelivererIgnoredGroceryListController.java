package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public class DelivererIgnoredGroceryListController {

    static final String GROCERYLISTNODE  = "grocerylists";
    static final String GROCERYLISTSTATUS = "status";

    GroceryListListener listener;
    FirebaseDatabase firebaseDatabase;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date currentDate = new Date();

    public DelivererIgnoredGroceryListController(GroceryListListener listListener){
        listener = listListener;
    }

    public void loadAllIgnoredGroceryLists(){

        if (firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();
        Query query = firebaseDatabase.getReference().child(GROCERYLISTNODE)
                .orderByChild(GROCERYLISTSTATUS).equalTo(GroceryListStatus.CREATED.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<GroceryListsModel> groceryList = new ArrayList<>();

                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    GroceryListsModel model = temp.getValue(GroceryListsModel.class);
                    model.setGrocerylist_key(temp.getKey());
                    groceryList.add(model);
                }
                listener.groceryListReceived(filterActiveLists(groceryList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private ArrayList<GroceryListsModel> filterActiveLists(ArrayList<GroceryListsModel> allActiveLists){
        ArrayList<GroceryListsModel> ignoredList = new ArrayList<>();
        List<String> userIgnoredListKeys = CurrentUser.currentUser.getIgnoredLists();
        for(int i = 0; i < allActiveLists.size(); i++){
            if(userIgnoredListKeys.contains(allActiveLists.get(i).getGrocerylist_key()) && groceryListDateValid(allActiveLists.get(i).getEnd_date())){
                ignoredList.add(allActiveLists.get(i));
            }
        }

        return ignoredList;

    }



    public boolean groceryListDateValid(String date){
        Date groceryListEndDate;

        try {
            groceryListEndDate = dateFormat.parse(date);
        }
        catch (Exception e){
            return false;
        }

        // ----------|----------------|---------------|------- TIMELINE
        // -----STARTDATEGL--------CURDATE--------ENDDATEGL-------
        //AKO JE CURRENTDATE PRIJE ENDDATEGL ZNACI DA GL JOS UVIJEK TRAJE

        if(currentDate.before(groceryListEndDate))
            return true;
        else
            return false;
    }

}
