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
        String email = mEmail;
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else return true;
    }

    /**
     * Provjera ispravnosti lozinke
     * @param mPassword
     * @return
     */
    public static boolean validatePassword(String mPassword){
        String password = mPassword;
        if (!PASSWORD_PATTERN.matcher(password).matches()){
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
        String firstPassword = mPassword;
        String secondPassword = mRetypedPassword;
        if(!firstPassword.equals(secondPassword)){
            return false;
        }else return true;
    }
}
