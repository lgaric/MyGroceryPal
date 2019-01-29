package hr.foi.air.mygrocerypal.myapplication.Model;

public class CategoriesModel {
    private String category_id;
    private String name;

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
     * Dobivanje Id-a kategorije
     * @return
     */
    public String getCategory_id() {
        return category_id;
    }

    /**
     * Postavljanje Id-a kategorije
     * @param category_id
     */
    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
