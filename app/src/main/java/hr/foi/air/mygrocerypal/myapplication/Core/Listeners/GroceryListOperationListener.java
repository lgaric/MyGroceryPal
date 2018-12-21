package hr.foi.air.mygrocerypal.myapplication.Core.Listeners;

import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListOperation;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;

public interface GroceryListOperationListener {
    void buttonPressedOnGroceryList(GroceryListsModel groceryListsModel, GroceryListOperation operation);
}
