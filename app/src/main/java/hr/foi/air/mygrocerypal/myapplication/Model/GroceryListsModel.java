package hr.foi.air.mygrocerypal.myapplication.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hr.foi.air.mygrocerypal.myapplication.Core.Enumerators.GroceryListStatus;

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

    /**
     * Postavljanje GL keya
     * @param grocerylist_key
     */
    public void setGrocerylist_key(String grocerylist_key) {
        this.grocerylist_key = grocerylist_key;
    }

    /**
     * Dobivanje geografske sirine
     * @return
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Postavljanje geografske sirine
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Dobivanje geografske sirine
     * @return
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Postavljanje geografske sirine
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Dobivanje GL keya
     * @return
     */
    public String getGrocerylist_key() {
        return grocerylist_key;
    }

    /**
     * Dobivanje provizije
     * @return
     */
    public String getCommision() {
        return commision;
    }

    /**
     * Postavljanje provizije
     * @param commision
     */
    public void setCommision(String commision) {
        this.commision = commision;
    }

    /**
     * Dobivanje dostavne adrese
     * @return
     */
    public String getDelivery_address() {
        return delivery_address;
    }

    /**
     * Postavljanje dostavne adrese
     * @param delivery_address
     */
    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    /**
     * Dobivanje dostavnog grada
     * @return
     */
    public String getDelivery_town() {
        return delivery_town;
    }

    /**
     * Postavljanje dostavnog grada
     * @param delivery_town
     */
    public void setDelivery_town(String delivery_town) {
        this.delivery_town = delivery_town;
    }

    /**
     * Dobivanje datuma zavrsetka Gl-a
     * @return
     */
    public String getEnd_date() {
        return end_date;
    }

    /**
     * Postavljanje datuma zavrsetka Gl-a
     * @param end_date
     */
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    /**
     * Dobivanje datuma pocetka Gl-a
     * @return
     */
    public String getStart_date() {
        return start_date;
    }

    /**
     * Postavljanje datuma pocetka Gl-a
     * @param start_date
     */
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    /**
     * Dobivanje statusa GLa
     * @return
     */
    public GroceryListStatus getStatus() {
        return status;
    }

    /**
     * Postavljanje statusa GLa
     * @param status
     */
    public void setStatus(GroceryListStatus status) {
        this.status = status;
    }

    /**
     * Dobivanje imena trgovine
     * @return
     */
    public String getStore_name() {
        return store_name;
    }

    /**
     * Postavljanje imena trgovine
     * @param store_name
     */
    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    /**
     * Dohvati ukupan iznos
     * @return
     */
    public String getTotal_price() {
        return total_price;
    }

    /**
     * Postavi ukupan iznos
     * @param total_price
     */
    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    /**
     * Dohvati ID korisnika koji je prihvatio GL
     * @return
     */
    public String getUser_accepted_id() {
        return user_accepted_id;
    }

    /**
     * Postavi ID korisnika koji je prihvatio GL
     * @param user_accepted_id
     */
    public void setUser_accepted_id(String user_accepted_id) {
        this.user_accepted_id = user_accepted_id;
    }

    /**
     * Dohvati ID korisnika koji je kreirao GL
     * @return
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * Postavi ID korisnika koji je kreirao GL
     * @param user_id
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    /**
     * Dohvati proizvode
     * @return
     */
    public List<GroceryListProductsModel> getProductsModels() {
        return productsModels;
    }

    /**
     * Postavi proizvode
     * @param productsModels
     */
    public void setProductsModels(List<GroceryListProductsModel> productsModels) {
        this.productsModels = productsModels;
    }
}
