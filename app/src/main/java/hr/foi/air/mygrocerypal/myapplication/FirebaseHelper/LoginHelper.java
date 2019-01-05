package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.LoginListener;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public class LoginHelper extends FirebaseBaseHelper{
    private LoginListener mLoginListener;
    private LinearLayout mProgress;
    private LinearLayout mNameAndLogoApp;

    public LoginHelper(LoginListener mLoginListener){
        this.mContext = (Context) mLoginListener;
        this.mLoginListener = mLoginListener;
    }

    /**
     * Prijava korisnika
     * @param mUsername
     * @param mPassword
     * @param progress
     * @param logo
     */
    public void login(String mUsername, String mPassword, LinearLayout progress, LinearLayout logo){
        if(mUsername.isEmpty() || mPassword.isEmpty()) {
            mLoginListener.onStatusFailed("Ispunite odgovarajuća polja!");
            return;
        }

        this.mProgress = progress;
        this.mNameAndLogoApp = logo;

        if(isNetworkAvailable()) {
            mAuth.signInWithEmailAndPassword(mUsername, mPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    getUserInformation(mAuth.getCurrentUser().getUid());
                                } else
                                    mLoginListener.onStatusFailed("Email nije verificiran");
                            } else {
                                String errorMessage = checkErrorCode(((FirebaseAuthException) task.getException()).getErrorCode());
                                mLoginListener.onStatusFailed(errorMessage);
                            }
                        }
                    });
        }
        else
            showInternetMessageWarning();
    }

    /**
     * Dohvati razlog neuspjesne prijave
     * @param mError
     * @return
     */
    private String checkErrorCode(String mError){
        String errorMessage;

        switch(mError) {
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

    /**
     * Dohvati sve informacije za trenutnog korisnika koji se logira
     * @param mUserUID
     */
    private void getUserInformation(String mUserUID){

        mNameAndLogoApp.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);


        if(isNetworkAvailable()) {
            mReference = mDatabase.getReference().child(USERNODE).child(mUserUID);

            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel temp = dataSnapshot.getValue(UserModel.class);
                    if (temp == null) {
                        mLoginListener.onStatusFailed(checkErrorCode(null));
                        mProgress.setVisibility(View.GONE);
                        mNameAndLogoApp.setVisibility(View.VISIBLE);
                    }
                    else {
                        CurrentUser.getCurrentUser = temp;
                        CurrentUser.getCurrentUser.setUserUID(dataSnapshot.getKey());
                        getUserIgnoredLists(dataSnapshot.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else {
            showInternetMessageWarning();
            mNameAndLogoApp.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        }
    }

    /**
     * Dohvati sve ignorirane liste trenutnog korisnika
     * @param mUserUID
     */
    private void getUserIgnoredLists(String mUserUID) {
        if(isNetworkAvailable()) {
            mReference = mDatabase.getReference().child(USERIGNOREDLISTNODE).child(mUserUID);

            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> ingoredLists = new ArrayList<>();
                    for (DataSnapshot temp : dataSnapshot.getChildren())
                        ingoredLists.add(temp.getKey());

                    CurrentUser.getCurrentUser.setIgnoredLists(ingoredLists);
                    mProgress.setVisibility(View.GONE);
                    mLoginListener.onStatusSuccess();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else {
            showInternetMessageWarning();
            mNameAndLogoApp.setVisibility(View.VISIBLE);
            mProgress.setVisibility(View.GONE);
        }
    }
}
