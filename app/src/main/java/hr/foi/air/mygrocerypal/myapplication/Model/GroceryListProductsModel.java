package hr.foi.air.mygrocerypal.myapplication.Model;

import java.io.Serializable;

public class GroceryListProductsModel implements Serializable {
    private String grocery_list_key;
    private String product_key;
    private int bought;
    private String name;
    private double price;
    private int quantity;

    /**
     * Dobivanje GL id-a
     * @return
     */
    public String getGrocery_list_key() {
        return grocery_list_key;
    }

    /**
     * Postavljanje GL id-a
     * @param grocery_list_key
     */
    public void setGrocery_list_key(String grocery_list_key) {
        this.grocery_list_key = grocery_list_key;
    }

    /**
     * Dobivanje kupljenosti
     * @return
     */
    public int getBought() {
        return bought;
    }

    /**
     * Postavljanje kupljenosti
     * @param bought
     */
    public void setBought(int bought) {
        this.bought = bought;
    }

    /**
     * Dobivanje imena
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Postavljanje imena
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Dobivanje cijene
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * Postavljanje cijene
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Dobivanje kolicine
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Postavljanje kolicine
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Dobivanje ProductKey-a
     * @return
     */
    public String getProduct_key() {
        return product_key;
    }

    /**
     * Postavljanje ProductKey-a
     * @param product_key
     */
    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }
}
