package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidateInputs {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");

    /**
     * Konstruktor
     * @param mEmail
     * @return
     */
    public static boolean validateEmail(String mEmail){
        if(!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            return false;
        }else return true;
    }

    /**
     * Provjera ispravnosti lozinke
     * @param mPassword
     * @return
     */
    public static boolean validatePassword(String mPassword){
        if (!PASSWORD_PATTERN.matcher(mPassword).matches()){
            return false;
        }else return true;
    }

    /**
     * Ponovna provjera ispravnosti lozinke
     * @param mPassword
     * @param mRetypedPassword
     * @return
     */
    public static boolean validateRetypedPassword(String mPassword, String mRetypedPassword){
        if(!mPassword.equals(mRetypedPassword)){
            return false;
        }else return true;
    }
}
