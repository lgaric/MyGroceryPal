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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.GroceryListStatusListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class DelivererGroceryListHelper extends FirebaseBaseHelper{
    private GroceryListStatusListener mGroceryListStatusListener;

    DateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date mCurrentDate;

    /**
     * Konstruktor
     * @param mGroceryListStatusListener
     */
    public DelivererGroceryListHelper(GroceryListStatusListener mGroceryListStatusListener){
        this.mContext = ((Fragment)mGroceryListStatusListener).getContext();
        this.mGroceryListStatusListener = mGroceryListStatusListener;
        mCurrentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCurrentDate);
        cal.add(Calendar.DATE, -1);
        mCurrentDate = cal.getTime();
    }

    /**
     * Ucitaj sve aktivne GL-ove
     */
    private void loadAllGroceryListsByStatus(final String mOption) {
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
                    if(mOption == "active") {
                        if(mGroceryListStatusListener != null)
                            mGroceryListStatusListener.groceryListReceived(getAllActiveLists(groceryList), GroceryListStatus.ACCEPTED);
                    }
                    else if (mOption == "ignored") {
                        if(mGroceryListStatusListener != null)
                            mGroceryListStatusListener.groceryListReceived(getAllUserIgnoredLists(groceryList), GroceryListStatus.ACCEPTED);
                    }
                    else if(mOption == "accepted") {
                        if(mGroceryListStatusListener != null)
                            mGroceryListStatusListener.groceryListReceived(getOnlyWithStatusAccepted(groceryList), GroceryListStatus.ACCEPTED);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }else
            showInternetMessageWarning();
    }

    /**
     * Aktivni GL-ovi
     */
    public void getAllActiveGroceryLists(){
        mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                .orderByChild(GROCERYLISTSTATUSNODE).equalTo(GroceryListStatus.CREATED.toString());
        loadAllGroceryListsByStatus("active");
    }

    /**
     * Ignorirani GL-ovi
     */
    public void getAllIgnoredGroceryLists(){
        mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                .orderByChild(GROCERYLISTSTATUSNODE).equalTo(GroceryListStatus.CREATED.toString());
        loadAllGroceryListsByStatus("ignored");
    }

    /**
     * Prihvaceni GL-ovi
     */
    public void getAllAcceptedListsByCurrentUser(){
        mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                .orderByChild(USERACCEPTEDIDNODE).equalTo(CurrentUser.getCurrentUser.getUserUID());
        loadAllGroceryListsByStatus("accepted");
    }

    /**
     * Promijeni status GL-a
     * @param mGroceryListID
     * @param mGroceryListStatus
     * @return
     */
    public String acceptGroceryList(final String mGroceryListID, final String mGroceryListStatus)
    {
        if(isNetworkAvailable()){
            if(GroceryListStatus.valueOf(mGroceryListStatus).equals(GroceryListStatus.ACCEPTED)
                    || GroceryListStatus.valueOf(mGroceryListStatus).equals(GroceryListStatus.FINISHED)) {
                return mContext.getResources().getString(R.string.groceryListAlreadyAccepted);
            }

            try{
                mReference = mDatabase.getReference();
                mReference.child(GROCERYLISTSNODE).child(mGroceryListID).child(GROCERYLISTSTATUSNODE).setValue(GroceryListStatus.ACCEPTED);
                mReference.child(GROCERYLISTSNODE).child(mGroceryListID).child(USERACCEPTEDIDNODE).setValue(CurrentUser.getCurrentUser.getUserUID());
                return mContext.getResources().getString(R.string.groceryListAccepted);
            }catch(Exception e) {
                Log.e(getClass().toString(), e.getMessage());
                return mContext.getResources().getString(R.string.acceptGLError);
            }
        }else
            return mContext.getResources().getString(R.string.noInternetConnectionMessage);
    }

    /**
     * Provjeri status GL-a
     * @param mGroceryListID
     * @param mOperation
     */
    public void checkGroceryListStatus(final String mGroceryListID, final GroceryListOperation mOperation) {
        if(isNetworkAvailable()){
            mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                    .child(mGroceryListID).child(GROCERYLISTSTATUSNODE);

            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mGroceryListStatusListener.groceryListStatusReceived(mGroceryListID, dataSnapshot.getValue().toString(), mOperation);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mGroceryListStatusListener.groceryListStatusReceived(mGroceryListID, "", mOperation);
                }
            });
        }else
            showInternetMessageWarning();
    }

    /**
     * Makni GL-ove koje netreba pokazati
     * @param mAllActiveLists
     * @return
     */
    private ArrayList<GroceryListsModel> getAllActiveLists(ArrayList<GroceryListsModel> mAllActiveLists){
        ArrayList<GroceryListsModel> mActiveList = new ArrayList<>();
        List<String> ignoredLists = CurrentUser.getCurrentUser.getIgnoredLists();

        //1 UVJET -> IZBACI SVE IGNORIRANE KORISNIKOVE LISTE
        //2 UVJET -> IZBACI SVE NJEGOVE LISTE (KORISNIK NE MOŽE PRIHVATITI SVOJU LISTU)
        //3 UVJET -> IZBACI SVE STARE LISTE (IZBACI SVE LISTE GDJE JE CURRENTDATE > GROCERYLISTENDDATE

        for(int i = 0; i < mAllActiveLists.size(); i++){
            if(!ignoredLists.contains(mAllActiveLists.get(i).getGrocerylist_key())
                    && !compareUsersId(CurrentUser.getCurrentUser.getUserUID(), mAllActiveLists.get(i).getUser_id())
                    && groceryListDateValid(mAllActiveLists.get(i).getEnd_date())){
                mActiveList.add(mAllActiveLists.get(i));
            }
        }

        return mActiveList;
    }

    /**
     * Pronadi ignorirane liste od strane korisnika
     * @param mGroceryList
     * @return
     */
    public ArrayList<GroceryListsModel> getAllUserIgnoredLists(ArrayList<GroceryListsModel> mGroceryList){
        ArrayList<GroceryListsModel> mUserIgnoredList = new ArrayList<>();
        for(int i = 0; i < mGroceryList.size(); i++){
            if(CurrentUser.getCurrentUser.getIgnoredLists().contains(mGroceryList.get(i).getGrocerylist_key()))
                mUserIgnoredList.add(mGroceryList.get(i));
        }
        return mUserIgnoredList;
    }

    /**
     * GL-ovi s statusom prihvacenih
     * @param mGroceryList
     * @return
     */
    public ArrayList<GroceryListsModel> getOnlyWithStatusAccepted(ArrayList<GroceryListsModel> mGroceryList) {
        ArrayList<GroceryListsModel> mAcceptedLists = new ArrayList<>();
        for (int i = 0; i < mGroceryList.size(); i++) {
            if(mGroceryList.get(i).getStatus().equals(GroceryListStatus.ACCEPTED)){
                mAcceptedLists.add(mGroceryList.get(i));
            }
        }
        return mAcceptedLists;
    }
    /**
     * Usporedi korisničke ID-eve
     * @param mCurrentUser
     * @param mGroceryListCreator
     * @return
     */
    public boolean compareUsersId(String mCurrentUser, String mGroceryListCreator){
        if(mCurrentUser.equals(mGroceryListCreator))
            return true;
        return false;
    }

    /**
     * Provjeri datum GL-a
     *         // ----------|----------------|---------------|------- TIMELINE
     *         // -----STARTDATEGL--------CURDATE--------ENDDATEGL-------
     *         //AKO JE CURRENTDATE PRIJE ENDDATEGL ZNACI DA GL JOS UVIJEK TRAJE
     * @param mDate
     * @return
     */
    public boolean groceryListDateValid(String mDate){
        Date mGroceryListDate;

        try {
            mGroceryListDate = mDateFormat.parse(mDate);
        }
        catch (Exception e){
            return false;
        }

        if(mCurrentDate.before(mGroceryListDate))
            return true;
        else
            return false;
    }

    /**
     * Ignoriraj GL
     * @param mGroceryListID
     * @return
     */
    public String ignoreGroceryList(String mGroceryListID) {
        if(isNetworkAvailable()){
            try{
                mReference = mDatabase.getReference().child(USERIGNOREDLISTNODE).child(CurrentUser.getCurrentUser.getUserUID())
                        .child(mGroceryListID);
                mReference.setValue(true);
                CurrentUser.getCurrentUser.getIgnoredLists().add(mGroceryListID);
                return mContext.getResources().getString(R.string.listIgnored);
            }catch(Exception e) {
                Log.e(getClass().toString(), e.getMessage());
                return mContext.getResources().getString(R.string.listIgnoredError);
            }
        }else
            return mContext.getResources().getString(R.string.noInternetConnectionMessage);

    }

    public void getUserFinishedGroceryLists(){
        mQuery = mDatabase.getReference().child(GROCERYLISTSNODE)
                .orderByChild(USERACCEPTEDIDNODE).equalTo(CurrentUser.getCurrentUser.getUserUID());

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

                    ArrayList<GroceryListsModel> temp = new ArrayList<>();
                    for (GroceryListsModel model : groceryList) {
                        if(model.getStatus() == GroceryListStatus.FINISHED)
                            temp.add(model);
                    }

                    if(mGroceryListStatusListener != null)
                        mGroceryListStatusListener.groceryListReceived(temp, GroceryListStatus.FINISHED);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }else
            showInternetMessageWarning();
    }

    /**
     * Ponisti ignoriranje Gla
     * @param mGroceryListKey
     * @return
     */
    public String returnGroceryListFromIgnored(String mGroceryListKey) {
        if(isNetworkAvailable()){
            try {
                mDatabase.getReference().child(USERIGNOREDLISTNODE).child(CurrentUser.getCurrentUser.getUserUID())
                        .child(mGroceryListKey).removeValue();
                CurrentUser.getCurrentUser.getIgnoredLists().remove(mGroceryListKey);
                return mContext.getResources().getString(R.string.listReturned);
            }catch (Exception e){
                Log.e(getClass().toString(), e.getMessage());
                return mContext.getResources().getString(R.string.listReturnedError);
            }
        }else
            return mContext.getResources().getString(R.string.noInternetConnectionMessage);
    }
}
