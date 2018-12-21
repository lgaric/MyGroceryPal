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

import static hr.foi.air.mygrocerypal.LocationBasedOnAddress.GetLocation;

public class RegistrationHelper extends FirebaseBaseHelper{
    private RegistrationListener listener;

    public RegistrationHelper(RegistrationListener listener) {
        this.context = (Context)listener;
        this.listener = listener;
    }

    /**
     * Registriraj korisnika
     * @param newUser
     */
    public void registration(UserModel newUser){
        final UserModel user = newUser;
        Location userLocation = LocationBasedOnAddress.GetLocation(user.getAddress() + ", " + user.getTown() + ", Croatia", (Context)listener);
        if(userLocation != null){
            user.setLongitude(userLocation.getLongitude());
            user.setLatitude(userLocation.getLatitude());
        }else{
            listener.onRegistrationFail("Greška prilikom dohvaćanja lokacije!");
            return;
        }


        if(isNetworkAvailable()) {
            mQuery = mDatabase.getReference().child("users").orderByChild("username").equalTo(user.getUsername());
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //username exists
                    if (dataSnapshot.getChildrenCount() > 0) {
                        listener.showToastRegistration("Korisničko ime je već u upotrebi!");
                    } else {
                        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final String uId = mAuth.getCurrentUser().getUid();
                                    mDatabase.getReference().child("users").child(uId).setValue(user);
                                    mAuth.getCurrentUser().sendEmailVerification();
                                    mAuth.signOut();
                                    listener.onRegistrationSuccess("Registracija uspješna. Molimo potvrdite email!");
                                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    listener.onRegistrationFail("Već postoji račun s navedenom email adresom!");
                                } else {
                                    listener.onRegistrationFail("Greška prilikom registracije!");
                                }
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
            showInternetMessageWarning();
    }

    /**
     * Pozovi metodu za registraciju ako je input korisnika valjan
     * @param newUser
     * @param retypedPassword
     */
    public void registerUser(UserModel newUser, String retypedPassword) {
        if (InputCorrect(newUser, retypedPassword))
            registration(newUser);
        else
            listener.onRegistrationFail("Molimo pravilno ispunite sve podatke!");
    }

    /**
     * Provjeri valjanost korisnikovog inputa
     * @param newUser
     * @param retypedPassword
     * @return
     */
    private boolean InputCorrect(UserModel newUser, String retypedPassword){
        if(newUser.getFirst_name().length() > 0 && newUser.getLast_name().length() > 0 &&
                newUser.getUsername().length() > 0 && newUser.getTown().length() > 0 &&
                newUser.getAddress().length() > 0 && newUser.getPhone_number().length() > 0 &&
                newUser.getBirth_date().length() > 0 && ValidateInputs.validateEmail(newUser.getEmail())
                && ValidateInputs.validatePassword(newUser.getPassword()) && ValidateInputs.validateRetypedPassword(newUser.getPassword(), retypedPassword))
            return true;
        else
            return false;
    }

}
