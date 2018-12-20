package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

public interface RegistrationListener {
    public void onRegistrationSuccess(String message);
    public void onRegistrationFail(String message);
    public void showToastRegistration(String message);
}
