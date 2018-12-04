package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import hr.foi.air.mygrocerypal.myapplication.Controller.Listeners.PasswordRecoveryListener;

public class PasswordRecoveryController {
    FirebaseAuth firebaseAuth;
    private PasswordRecoveryListener listener;

    public  PasswordRecoveryController(Context context){
        listener = (PasswordRecoveryListener) context;
    }


    public void sendRecoveryMail(String email){
        firebaseAuth = FirebaseAuth.getInstance();
        if(validateEmail(email)){
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                listener.onRecoverySuccess("Link za promjenu lozinke je poslan. Provjerite email!");
                            }
                            else{
                                listener.onRecoveryFail("Došlo je do greške, provjerite ispravnost email-a!");
                            }
                        }
                    });
        }else{
            listener.onRecoveryFail("Unesite ispravnu email adresu!");
        }

    }

    private boolean validateEmail(String emailTxt){
        String email = emailTxt.trim();

        if(email == null){
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else return true;
    }

}
