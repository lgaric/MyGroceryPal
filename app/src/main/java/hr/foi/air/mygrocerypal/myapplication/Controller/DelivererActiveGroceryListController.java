package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public class DelivererActiveGroceryListController {
    static final String GROCERYLISTNODE  = "grocerylists";
    static final String GROCERYLISTSTATUS = "status";

    GroceryListListener groceryListListener;
    GroceryListStatusListener groceryListStatusListener;
    FirebaseDatabase firebaseDatabase;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date currentDate = new Date();

    public DelivererActiveGroceryListController(GroceryListListener listListener){
        groceryListListener = listListener;
        groceryListStatusListener = (GroceryListStatusListener) listListener;
    }

    public void loadAllActiveGroceryLists() {

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
                groceryListListener.groceryListReceived(filterIgnoredLists(groceryList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String acceptGroceryList(final String groceryListID, final String groceryListStatus)
    {
        if(firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();

        String message = "";
        if(GroceryListStatus.valueOf(groceryListStatus).equals(GroceryListStatus.ACCEPTED)
                || GroceryListStatus.valueOf(groceryListStatus).equals(GroceryListStatus.FINISHED)) {
            message = "Kupovna lista je već prihvaćena";
            return message;
        }

        try{
            DatabaseReference db = firebaseDatabase.getReference();
            db.child(GROCERYLISTNODE).child(groceryListID).child(GROCERYLISTSTATUS).setValue(GroceryListStatus.ACCEPTED);
            db.child(GROCERYLISTNODE).child(groceryListID).child("user_accepted_id").setValue(CurrentUser.currentUser.getUserUID());
            message = "Prihvaćen odabir";
            return message;
        }catch(Exception e) {
            Log.e(getClass().toString(), e.getMessage());
            message = "Greška prilikom prihvaćanja narudžbe";
        }
        return message;
    }


    public void checkGroceryListStatus(final String groceryListID) {

        if(firebaseDatabase == null)
            firebaseDatabase = FirebaseDatabase.getInstance();

        Query query = firebaseDatabase.getReference().child(GROCERYLISTNODE)
                .child(groceryListID).child(GROCERYLISTSTATUS);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groceryListStatusListener.groceryListStatusReceived(groceryListID, dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                groceryListStatusListener.groceryListStatusReceived(groceryListID, "");
            }
        });

    }

    private ArrayList<GroceryListsModel> filterIgnoredLists(ArrayList<GroceryListsModel> allActiveLists){

        ArrayList<GroceryListsModel> validLists = new ArrayList<>();
        List<String> ignoredLists = CurrentUser.currentUser.getIgnoredLists();

        //1 UVJET -> IZBACI SVE IGNORIRANE KORISNIKOVE LISTE
        //2 UVJET -> IZBACI SVE NJEGOVE LISTE (KORISNIK NE MOŽE PRIHVATITI SVOJU LISTU)
        //3 UVJET -> IZBACI SVE STARE LISTE (IZBACI SVE LISTE GDJE JE CURRENTDATE > GROCERYLISTENDDATE

        for(int i = 0; i < allActiveLists.size(); i++){
            if(!ignoredLists.contains(allActiveLists.get(i).getGrocerylist_key())
                    && !compareUsersId(CurrentUser.currentUser.getUserUID(), allActiveLists.get(i).getUser_id())
                    && groceryListDateValid(allActiveLists.get(i).getEnd_date())){
                validLists.add(allActiveLists.get(i));
            }
        }

        return validLists;
    }

    public boolean compareUsersId(String currentUser, String groceryListCreator){
        if(currentUser.equals(groceryListCreator))
            return true;
        return false;
    }

    public boolean groceryListDateValid(String date){
        Date groceryListDate;

        try {
            groceryListDate = dateFormat.parse(date);
        }
        catch (Exception e){
            return false;
        }

        // ----------|----------------|---------------|------- TIMELINE
        // -----STARTDATEGL--------CURDATE--------ENDDATEGL-------
        //AKO JE CURRENTDATE PRIJE ENDDATEGL ZNACI DA GL JOS UVIJEK TRAJE

        if(currentDate.before(groceryListDate))
            return true;
        else
            return false;
    }


    //TODO
    //ZATIM GLEDAJ UDALJENOST IZMEDU GROCERYLISTA I KORISNIKA
    //   a) AKO KORISNIK NEMA UKLJUCEN GPS GLEDAJ LATITUDE I LANGITUTDE IZ KLASE CURRENTUSER
    //   b) AKO IMA UKLJUCENO GLEDAJ OD HRVOJA :-]
}
