package hr.foi.air.mygrocerypal.myapplication.Controller.Listeners;

import android.widget.EditText;

public interface ChangeProductQuantityListener {
    public void updateQuantity(int position, EditText productQuantity, int value);
    public void manuallySetQuantity();
}
