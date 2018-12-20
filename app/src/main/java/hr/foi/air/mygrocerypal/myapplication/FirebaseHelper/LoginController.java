package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.LoginListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public class LoginController extends FirebaseBaseHelper{
    private LoginListener listener;

    public  LoginController(Context context){
        this.context = context;
        listener = (LoginListener)context;
    }

    public void login(String username, String password){
        if(username.isEmpty() || password.isEmpty()) {
            listener.onStatusFailed("Ispunite odgovarajuća polja!");
            return;
        }

        if(isNetworkAvailable()) {
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    getUserInformation(mAuth.getCurrentUser().getUid());
                                } else
                                    listener.onStatusFailed("Email nije verificiran");
                            } else {
                                String errorMessage = checkErrorCode(((FirebaseAuthException) task.getException()).getErrorCode());
                                listener.onStatusFailed(errorMessage);
                            }
                        }
                    });
        }
        else
            showInternetMessageWarning();
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
        if(isNetworkAvailable()) {
            mReference = mDatabase.getReference().child(USERNODE).child(userUID);

            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel temp = dataSnapshot.getValue(UserModel.class);
                    if (temp == null)
                        listener.onStatusFailed(checkErrorCode(null));
                    else {
                        CurrentUser.currentUser = temp;
                        CurrentUser.currentUser.setUserUID(dataSnapshot.getKey());
                        getUserIgnoredLists(dataSnapshot.getKey());
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

    private void getUserIgnoredLists(String userUID) {
        if(isNetworkAvailable()) {
            mReference = mDatabase.getReference().child(USERIGNOREDLISTNODE).child(userUID);

            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> ingoredLists = new ArrayList<>();
                    for (DataSnapshot temp : dataSnapshot.getChildren())
                        ingoredLists.add(temp.getKey());

                    CurrentUser.currentUser.setIgnoredLists(ingoredLists);
                    listener.onStatusSuccess();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
            showInternetMessageWarning();
    }
}
