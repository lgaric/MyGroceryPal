package hr.foi.air.mygrocerypal.myapplication.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Core.GroceryListStatus;

public class GroceryListsModel implements Serializable {
    private String grocerylist_key;
    private String commision;
    private String delivery_address;
    private String delivery_town;
    private String end_date;
    private String start_date;
    private GroceryListStatus status;
    private String store_name;
    private String total_price;
    private String user_accepted_id;
    private String user_id;
    private Double longitude;
    private Double latitude;

    private List<GroceryListProductsModel> productsModels;

    public GroceryListsModel(){
        productsModels = new ArrayList<>();
    }

    //Konstruktor za dodavanje novog GL-a u CreateNewGroceryListController
    public GroceryListsModel(String commision, String delivery_address, String delivery_town, String end_date, String start_date,
                             GroceryListStatus status, String store_name, String total_price, String user_id, String user_accepted_id,
                            Double longitude, Double latitude) {
        this.commision = commision;
        this.delivery_address = delivery_address;
        this.delivery_town = delivery_town;
        this.end_date = end_date;
        this.start_date = start_date;
        this.status = status;
        this.store_name = store_name;
        this.total_price = total_price;
        this.user_id = user_id;
        this.user_accepted_id = user_accepted_id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void setGrocerylist_key(String grocerylist_key) {
        this.grocerylist_key = grocerylist_key;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getGrocerylist_key() {
        return grocerylist_key;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDelivery_town() {
        return delivery_town;
    }

    public void setDelivery_town(String delivery_town) {
        this.delivery_town = delivery_town;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public GroceryListStatus getStatus() {
        return status;
    }

    public void setStatus(GroceryListStatus status) {
        this.status = status;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUser_accepted_id() {
        return user_accepted_id;
    }

    public void setUser_accepted_id(String user_accepted_id) {
        this.user_accepted_id = user_accepted_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<GroceryListProductsModel> getProductsModels() {
        return productsModels;
    }

    public void setProductsModels(List<GroceryListProductsModel> productsModels) {
        this.productsModels = productsModels;
    }
}
