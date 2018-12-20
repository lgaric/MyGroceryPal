package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

public interface RegistrationListener {
   void onRegistrationSuccess(String message);
   void onRegistrationFail(String message);
   void showToastRegistration(String message);
}
