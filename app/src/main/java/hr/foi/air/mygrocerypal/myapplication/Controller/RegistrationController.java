package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.RegistrationListener;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public class RegistrationController {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private RegistrationListener listener;

    public RegistrationController(Context context) {
        listener = (RegistrationListener) context;
    }

    public void registration(UserModel newUser){
        final UserModel user = newUser;
        user.setLatitude(20.0);
        user.setLongitude(20.0);
        user.setRange(20.0);

        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        if(mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance();

        Query query = mDatabase.getReference().child("users").orderByChild("username").equalTo(user.getUsername());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //username exists
                if(dataSnapshot.getChildrenCount() > 0) {
                    listener.showToastRegistration("Korisničko ime je već u upotrebi!");
                }
                else{
                    mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final String uId = mAuth.getCurrentUser().getUid();
                                mDatabase.getReference().child("users").child(uId).setValue(user);
                                mAuth.getCurrentUser().sendEmailVerification();
                                mAuth.signOut();
                                listener.onRegistrationSuccess("Registracija uspješna. Molimo potvrdite email!");
                            }
                            else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                listener.onRegistrationFail("Već postoji račun s navedenom email adresom!");
                            }
                            else{
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

    public boolean validateEmail(String emailTxt){
        String email = emailTxt;
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else return true;
    }

    public boolean validatePassword(String passwordTxt){
        String password = passwordTxt;
        if (!PASSWORD_PATTERN.matcher(password).matches()){
            return false;
        }else return true;
    }

    public boolean validateRetypedPassword(String passwordTxt, String retypedPasswordTxt){
        String firstPassword = passwordTxt;
        String secondPassword = retypedPasswordTxt;
        if(!firstPassword.equals(secondPassword)){
            return false;
        }else return true;
    }

    public void registerUser(UserModel newUser, String retypedPassword) {
        if (InputCorrect(newUser, retypedPassword))
            registration(newUser);
        else
            listener.onRegistrationFail("Sva polja su obavezna!");

    }

    private boolean InputCorrect(UserModel newUser, String retypedPassword){
        if(newUser.getFirst_name().length() > 0 && newUser.getLast_name().length() > 0 &&
                newUser.getUsername().length() > 0 && newUser.getTown().length() > 0 &&
                newUser.getAddress().length() > 0 && newUser.getPhone_number().length() > 0 &&
                newUser.getBirth_date().length() > 0 && validateEmail(newUser.getEmail())
                && validatePassword(newUser.getPassword()) && validateRetypedPassword(newUser.getPassword(), retypedPassword))
            return true;
        else
            return false;
    }
}
