package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.PasswordRecoveryListener;

public class PasswordRecoveryHelper extends FirebaseBaseHelper{
    private PasswordRecoveryListener mPasswordRecoveryListener;

    public PasswordRecoveryHelper(PasswordRecoveryListener mPasswordRecoveryListener){
        this.mContext = (Context) mPasswordRecoveryListener;
        this.mPasswordRecoveryListener = mPasswordRecoveryListener;
    }

    /**
     * Pošalji email za kreiranje nove lozinke
     * @param mEmail
     */
    public void sendRecoveryMail(String mEmail){
        if(isNetworkAvailable()) {
            if (ValidateInputs.validateEmail(mEmail)) {
                mAuth.sendPasswordResetEmail(mEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mPasswordRecoveryListener.onRecoverySuccess("Link za promjenu lozinke je poslan. Provjerite email!");
                                } else {
                                    mPasswordRecoveryListener.onRecoveryFail("Došlo je do greške, provjerite ispravnost email-a!");
                                }
                            }
                        });
            } else {
                mPasswordRecoveryListener.onRecoveryFail("Unesite ispravnu email adresu!");
            }
        }
        else
            showInternetMessageWarning();
    }
}
