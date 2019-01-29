package hr.foi.air.mygrocerypal.myapplication.Model;

import com.example.filter.FilterableObject;

public class ProductsModel extends FilterableObject {
    private String product_key;
    private String category_id;
    private double current_price;
    private String image_url;
    private String store_id;
    private String store_name;

    /**
     * Dohvati ID kategorije
     * @return
     */
    public String getCategory_id() {
        return category_id;
    }

    /**
     * Postavi ID kategorije
     * @param category_id
     */
    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    /**
     * Dohvati ime kategorije
     * @return
     */
    public String getCategory_name() {
        return category_name;
    }

    /**
     * Postavi ime kategorije
     * @param category_name
     */
    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    /**
     * Dohvati trenutnu cijenu
     * @return
     */
    public double getCurrent_price() {
        return current_price;
    }

    /**
     * Postavi trenutnu cijenu
     * @param current_price
     */
    public void setCurrent_price(double current_price) {
        this.current_price = current_price;
    }

    /**
     * Dohvati URL slike
     * @return
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * Postavi URL slike
     * @param image_url
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    /**
     * Dohvati ime
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Postavi ime
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Dohvati ID trgovine
     * @return
     */
    public String getStore_id() {
        return store_id;
    }

    /**
     * Postavi ID trgovine
     * @param store_id
     */
    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    /**
     * Dohvati ime trgovine
     * @return
     */
    public String getStore_name() {
        return store_name;
    }

    /**
     * Postavi ime trgovine
     * @param store_name
     */
    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    /**
     * Dohvati Product_key
     * @return
     */
    public String getProduct_key() {
        return product_key;
    }

    /**
     * Postavi Product_key
     * @param product_key
     */
    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }
}
