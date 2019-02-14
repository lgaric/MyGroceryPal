package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.PasswordRecoveryListener;
import hr.foi.air.mygrocerypal.myapplication.R;

public class PasswordRecoveryHelper extends FirebaseBaseHelper{
    private PasswordRecoveryListener mPasswordRecoveryListener;

    /**
     * Konstruktor
     * @param mPasswordRecoveryListener
     */
    public PasswordRecoveryHelper(PasswordRecoveryListener mPasswordRecoveryListener){
        if(mPasswordRecoveryListener instanceof Fragment){
            this.mContext = ((Fragment) mPasswordRecoveryListener).getContext();
        }
        else{
            this.mContext = ((Context) mPasswordRecoveryListener);
        }

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
                                    if(mPasswordRecoveryListener != null)
                                        mPasswordRecoveryListener.onRecoverySuccess(mContext.getResources().getString(R.string.pwRecoverySuccess));
                                } else {
                                    if(mPasswordRecoveryListener != null)
                                        mPasswordRecoveryListener.onRecoveryFail(mContext.getResources().getString(R.string.pwRecoveryFail));
                                }
                            }
                        });
            } else {
                if(mPasswordRecoveryListener != null)
                    mPasswordRecoveryListener.onRecoveryFail(mContext.getResources().getString(R.string.emailError));
            }
        }
        else
            showInternetMessageWarning();
    }
}
