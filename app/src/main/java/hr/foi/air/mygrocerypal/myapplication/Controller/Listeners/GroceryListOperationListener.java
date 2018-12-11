package hr.foi.air.mygrocerypal.myapplication.Controller.Listeners;

import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public interface GroceryListOperationListener {
    void buttonPressedOnGroceryList(GroceryListsModel groceryListsModel, GroceryListOperation operation);
}
