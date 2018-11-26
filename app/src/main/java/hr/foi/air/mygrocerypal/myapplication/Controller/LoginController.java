package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.LoginListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public class LoginController {
    private LoginListener listener;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public  LoginController(Context context){
        listener = (LoginListener)context;
    }

    public void Login(String username, String password){

        if(username.isEmpty() || password.isEmpty()) {
            listener.onStatusFailed("Ispunite odgovarajuća polja!");
            return;
        }

        if(mAuth == null)
            mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(mAuth.getCurrentUser().isEmailVerified()) {
                                getUserInformation(mAuth.getCurrentUser().getUid());
                            }
                            else
                                listener.onStatusFailed("Email nije verificiran");
                        }
                        else{
                            String errorMessage = checkErrorCode(((FirebaseAuthException) task.getException()).getErrorCode());
                            listener.onStatusFailed(errorMessage);
                        }
                    }
                });

    }

    private String checkErrorCode(String error){
        String errorMessage;

        switch(error) {
            case "ERROR_INVALID_EMAIL":
                errorMessage ="Neispravan Email";
                break;
            case "ERROR_WRONG_PASSWORD":
                errorMessage = "Neispravna lozinka";
                break;
            case "ERROR_USER_NOT_FOUND":
                errorMessage = "Nepostojeći korisnik";
                break;
            default:
                errorMessage = "Greška";
                break;
        }

        return  errorMessage;
    }

    private void getUserInformation(String userUID){
        if(mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = mDatabase.getReference().child("users").child(userUID);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel temp = dataSnapshot.getValue(UserModel.class);
                if(temp == null)
                    listener.onStatusFailed(checkErrorCode(null));
                else {
                    CurrentUser.currentUser = temp;
                    listener.onStatusSuccess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
