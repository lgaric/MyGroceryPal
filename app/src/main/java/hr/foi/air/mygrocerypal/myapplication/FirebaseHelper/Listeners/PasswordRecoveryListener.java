package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

public interface PasswordRecoveryListener {
    void onRecoverySuccess(String message);
    void onRecoveryFail(String message);

}
