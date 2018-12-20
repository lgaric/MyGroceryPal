package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper;

import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidateInputs {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");

    public static boolean validateEmail(String emailTxt){
        String email = emailTxt;
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else return true;
    }

    public static boolean validatePassword(String passwordTxt){
        String password = passwordTxt;
        if (!PASSWORD_PATTERN.matcher(password).matches()){
            return false;
        }else return true;
    }

    public static boolean validateRetypedPassword(String passwordTxt, String retypedPasswordTxt){
        String firstPassword = passwordTxt;
        String secondPassword = retypedPasswordTxt;
        if(!firstPassword.equals(secondPassword)){
            return false;
        }else return true;
    }
}
