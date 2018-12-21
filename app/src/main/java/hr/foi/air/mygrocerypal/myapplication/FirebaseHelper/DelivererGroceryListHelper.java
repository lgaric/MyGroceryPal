package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public class DelivererGroceryListHelper extends FirebaseBaseHelper{
    GroceryListStatusListener groceryListStatusListener;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date currentDate = new Date();

    public DelivererGroceryListHelper(GroceryListStatusListener listListener){
        this.context = ((Fragment)listListener).getContext();
        groceryListStatusListener = listListener;
    }

    /**
     * Ucitaj sve aktivne GL-ove
     */
    private void loadAllGroceryListsByStatus(final String option) {
        if(isNetworkAvailable()){
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<GroceryListsModel> groceryList = new ArrayList<>();

                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        GroceryListsModel model = temp.getValue(GroceryListsModel.class);
                        model.setGrocerylist_key(temp.getKey());
                        groceryList.add(model);
                    }
                    if(option == "active")
                        groceryListStatusListener.groceryListReceived(getAllActiveLists(groceryList));
                    else if (option == "ignored")
                        groceryListStatusListener.groceryListReceived(getAllUserIgnoredLists(groceryList));
                    //else //Dohvati prihvacene liste
                        //groceryListStatusListener.groceryListReceived(getAllActiveLists(groceryList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else
            showInternetMessageWarning();
    }

    public void getAllActiveGroceryLists(){
        mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                .orderByChild(GROCERYLISTSTATUSNODE).equalTo(GroceryListStatus.CREATED.toString());
        loadAllGroceryListsByStatus("active");
    }

    public void getAllIgnoredGroceryLists(){
        mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                .orderByChild(GROCERYLISTSTATUSNODE).equalTo(GroceryListStatus.CREATED.toString());
        loadAllGroceryListsByStatus("ignored");
    }

    public void getAllAcceptedListsByCurrentUser(){
        // TODO Implement method
    }

    /**
     * Promijeni status GL-a
     * @param groceryListID
     * @param groceryListStatus
     * @return
     */
    public String acceptGroceryList(final String groceryListID, final String groceryListStatus)
    {
        if(isNetworkAvailable()){
            if(GroceryListStatus.valueOf(groceryListStatus).equals(GroceryListStatus.ACCEPTED)
                    || GroceryListStatus.valueOf(groceryListStatus).equals(GroceryListStatus.FINISHED)) {
                return "Kupovna lista je već prihvaćena";
            }

            try{
                mReference = mDatabase.getReference();
                mReference.child(GROCERYLISTSNODE).child(groceryListID).child(GROCERYLISTSTATUSNODE).setValue(GroceryListStatus.ACCEPTED);
                mReference.child(GROCERYLISTSNODE).child(groceryListID).child(USERACCEPTEDIDNODE).setValue(CurrentUser.currentUser.getUserUID());
                return "Prihvaćen odabir";
            }catch(Exception e) {
                Log.e(getClass().toString(), e.getMessage());
                return "Greška prilikom prihvaćanja narudžbe";
            }
        }else
            return "Potrebna je internet veza!";
    }

    /**
     * Provjeri status GL-a
     * @param groceryListID
     * @param operation
     */
    public void checkGroceryListStatus(final String groceryListID, final GroceryListOperation operation) {
        if(isNetworkAvailable()){
            mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                    .child(groceryListID).child(GROCERYLISTSTATUSNODE);

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    groceryListStatusListener.groceryListStatusReceived(groceryListID, dataSnapshot.getValue().toString(), operation);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    groceryListStatusListener.groceryListStatusReceived(groceryListID, "", operation);
                }
            });
        }else
            showInternetMessageWarning();
    }

    /**
     * Makni GL-ove koje netreba pokazati
     * @param allActiveLists
     * @return
     */
    private ArrayList<GroceryListsModel> getAllActiveLists(ArrayList<GroceryListsModel> allActiveLists){
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

    /**
     * Pronadi ignorirane liste od strane korisnika
     * @param groceryList
     * @return
     */
    public ArrayList<GroceryListsModel> getAllUserIgnoredLists(ArrayList<GroceryListsModel> groceryList){
        ArrayList<GroceryListsModel> temporary = new ArrayList<>();
        for(int i = 0; i < groceryList.size(); i++){
            if(CurrentUser.currentUser.getIgnoredLists().contains(groceryList.get(i).getGrocerylist_key()))
                temporary.add(groceryList.get(i));
        }
        return temporary;
    }

    /**
     * Usporedi korisničke ID-eve
     * @param currentUser
     * @param groceryListCreator
     * @return
     */
    public boolean compareUsersId(String currentUser, String groceryListCreator){
        if(currentUser.equals(groceryListCreator))
            return true;
        return false;
    }

    /**
     * Provjeri datum GL-a
     *         // ----------|----------------|---------------|------- TIMELINE
     *         // -----STARTDATEGL--------CURDATE--------ENDDATEGL-------
     *         //AKO JE CURRENTDATE PRIJE ENDDATEGL ZNACI DA GL JOS UVIJEK TRAJE
     * @param date
     * @return
     */
    public boolean groceryListDateValid(String date){
        Date groceryListDate;

        try {
            groceryListDate = dateFormat.parse(date);
        }
        catch (Exception e){
            return false;
        }

        if(currentDate.before(groceryListDate))
            return true;
        else
            return false;
    }

    public String ignoreGroceryList(String groceryListID) {
        if(isNetworkAvailable()){
            try{
                mReference = mDatabase.getReference().child(USERIGNOREDLISTNODE).child(CurrentUser.currentUser.getUserUID())
                        .child(groceryListID);
                mReference.setValue(true);
                CurrentUser.currentUser.getIgnoredLists().add(groceryListID);
                return "Lista ignorirana!";
            }catch(Exception e) {
                Log.e(getClass().toString(), e.getMessage());
                return "Greška prilikom ignoriranja narudžbe!";
            }
        }else
            return "Potrebna je internet veza!";

    }

    public String returnGroceryListFromIgnored(String grocerylist_key) {
        if(isNetworkAvailable()){
            try {
                mDatabase.getReference().child(USERIGNOREDLISTNODE).child(CurrentUser.currentUser.getUserUID())
                        .child(grocerylist_key).removeValue();
                CurrentUser.currentUser.getIgnoredLists().remove(grocerylist_key);
                return "Lista vraćena!";
            }catch (Exception e){
                Log.e(getClass().toString(), e.getMessage());
                return "Greška prilikom ignoriranja narudžbe";
            }
        }else
            return "Potrebna je internet veza!";
    }
}
