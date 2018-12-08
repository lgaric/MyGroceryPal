package hr.foi.air.mygrocerypal.myapplication.Controller.Listeners;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;

public interface GroceryListUserListener {
        public void groceryListUserReceived(@Nullable  UserModel groceryListUser);

}
