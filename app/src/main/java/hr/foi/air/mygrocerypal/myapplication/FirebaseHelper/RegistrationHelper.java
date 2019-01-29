package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import hr.foi.air.mygrocerypal.LocationBasedOnAddress;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.RegistrationListener;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class RegistrationHelper extends FirebaseBaseHelper{
    private RegistrationListener mRegistrationListener;

    /**
     * Konstruktor
     * @param mRegistrationListener
     */
    public RegistrationHelper(RegistrationListener mRegistrationListener) {
        this.mContext = (Context) mRegistrationListener;
        this.mRegistrationListener = mRegistrationListener;
    }

    /**
     * Registriraj korisnika
     * @param mNewUser
     */
    public void registration(UserModel mNewUser){
        final UserModel user = mNewUser;
        Location userLocation = LocationBasedOnAddress.GetLocation(user.getAddress() + ", " + user.getTown() + ", Croatia", (Context) mRegistrationListener);
        if(userLocation != null){
            user.setLongitude(userLocation.getLongitude());
            user.setLatitude(userLocation.getLatitude());
        }else{
            mRegistrationListener.onRegistrationFail(mContext.getResources().getString(R.string.fetchLocationError));
            return;
        }


        if(isNetworkAvailable()) {
            mQuery = mDatabase.getReference().child("users").orderByChild("username").equalTo(user.getUsername());
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //username exists
                    if (dataSnapshot.getChildrenCount() > 0) {
                        mRegistrationListener.showToastRegistration(mContext.getResources().getString(R.string.usernameInUse));
                    } else {
                        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final String uId = mAuth.getCurrentUser().getUid();
                                    mDatabase.getReference().child("users").child(uId).setValue(user);
                                    mAuth.getCurrentUser().sendEmailVerification();
                                    mAuth.signOut();
                                    mRegistrationListener.onRegistrationSuccess(mContext.getResources().getString(R.string.registrationSuccess));
                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    mRegistrationListener.onRegistrationFail(mContext.getResources().getString(R.string.emailInUse));
                                } else {
                                    mRegistrationListener.onRegistrationFail(mContext.getResources().getString(R.string.registrationFail));
                                }
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Do nothing
                }
            });
        }
        else
            showInternetMessageWarning();
    }

    /**
     * Pozovi metodu za registraciju ako je input korisnika valjan
     * @param mNewUser
     * @param mRetypedPassword
     */
    public void registerUser(UserModel mNewUser, String mRetypedPassword) {
        if (InputCorrect(mNewUser, mRetypedPassword))
            registration(mNewUser);
        else
            mRegistrationListener.onRegistrationFail(mContext.getResources().getString(R.string.checkInputMessage));
    }

    /**
     * Provjeri valjanost korisnikovog inputa
     * @param mNewUser
     * @param mRetypedPassword
     * @return
     */
    private boolean InputCorrect(UserModel mNewUser, String mRetypedPassword){
        if(mNewUser.getFirst_name().length() > 0 && mNewUser.getLast_name().length() > 0 &&
                mNewUser.getUsername().length() > 0 && mNewUser.getTown().length() > 0 &&
                mNewUser.getAddress().length() > 0 && mNewUser.getPhone_number().length() > 0 &&
                mNewUser.getBirth_date().length() > 0 && ValidateInputs.validateEmail(mNewUser.getEmail())
                && ValidateInputs.validatePassword(mNewUser.getPassword()) && ValidateInputs.validateRetypedPassword(mNewUser.getPassword(), mRetypedPassword)
                && mNewUser.getUser_iban().length() == 21)
            return true;
        else
            return false;
    }

}
