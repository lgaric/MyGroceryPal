package hr.foi.air.mygrocerypal.myapplication.Model;

import java.util.ArrayList;

public class StoresModel {
    private String storeId;
    private String name;
    public ArrayList<ProductsModel> listOfProducts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }


}
