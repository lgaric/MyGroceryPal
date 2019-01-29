package hr.foi.air.mygrocerypal.myapplication.Model;

public class StoresModel {
    private String name;
    private String store_id;

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
     * Dohvati Store_id
     * @return
     */
    public String getStore_id() {
        return store_id;
    }

    /**
     * Postavi Store_id
     * @param store_id
     */
    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }
}
