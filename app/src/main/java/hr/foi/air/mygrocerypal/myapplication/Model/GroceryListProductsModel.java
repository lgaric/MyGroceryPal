package hr.foi.air.mygrocerypal.myapplication.Model;

public class GroceryListProductsModel {
    private String grocery_list_key;
    private String product_key;
    private int bought;
    private String name;
    private double price;
    private int quantity;

   public GroceryListProductsModel(){

   }

    public String getGrocery_list_key() {
        return grocery_list_key;
    }

    public void setGrocery_list_key(String grocery_list_key) {
        this.grocery_list_key = grocery_list_key;
    }

    public int getBought() {
        return bought;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct_key() {
        return product_key;
    }

    public void setProduct_key(String product_key) {
        this.product_key = product_key;
    }
}
