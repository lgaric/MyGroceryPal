package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

public interface RegistrationListener {
   void onRegistrationSuccess(String mMessage);
   void onRegistrationFail(String mMessage);
   void showToastRegistration(String mMessage);
}
