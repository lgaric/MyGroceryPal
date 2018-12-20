package hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners;

import android.support.annotation.Nullable;

import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public interface GroceryListUserListener {
        void groceryListUserReceived(@Nullable  UserModel groceryListUser);

}
