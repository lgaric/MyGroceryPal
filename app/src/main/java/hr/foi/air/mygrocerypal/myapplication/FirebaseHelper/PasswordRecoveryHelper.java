package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.PasswordRecoveryListener;

public class PasswordRecoveryHelper extends FirebaseBaseHelper{
    private PasswordRecoveryListener listener;

    public PasswordRecoveryHelper(PasswordRecoveryListener listener){
        this.context = (Context)listener;
        this.listener = listener;
    }

    /**
     * Pošalji email za kreiranje nove lozinke
     * @param email
     */
    public void sendRecoveryMail(String email){
        if(isNetworkAvailable()) {
            if (ValidateInputs.validateEmail(email)) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    listener.onRecoverySuccess("Link za promjenu lozinke je poslan. Provjerite email!");
                                } else {
                                    listener.onRecoveryFail("Došlo je do greške, provjerite ispravnost email-a!");
                                }
                            }
                        });
            } else {
                listener.onRecoveryFail("Unesite ispravnu email adresu!");
            }
        }
        else
            showInternetMessageWarning();
    }
}
