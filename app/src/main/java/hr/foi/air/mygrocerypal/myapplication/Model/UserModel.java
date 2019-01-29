package hr.foi.air.mygrocerypal.myapplication.Model;


import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    private String userUID;
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private String password;
    private String town;
    private String address;
    private String phone_number;
    private String birth_date;
    private String user_iban;
    private Double longitude;
    private Double latitude;
    List<String> ignoredLists;

    /**
     * Konstruktor
     */
    public UserModel(){}

    /**
     * Dohvat geografske sirine
     * @return
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Postavi geografsku sirinu
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Dohvat geografske duzine
     * @return
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Postavljanje geografske duzine
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Dohvati ID usera
     * @return
     */
    public String getUserUID() {
        return userUID;
    }

    /**
     * Postavi ID usera
     * @param userUID
     */
    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    /**
     * Dohvati ime
     * @return
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Postavi ime
     * @param first_name
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Dohvati prezime
     * @return
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Postavi prezime
     * @param last_name
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Dohvati username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Postavi username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Dohvati email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Postavi email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Dohvati lozinku
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Postavi lozinku
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Dohvati grad
     * @return
     */
    public String getTown() {
        return town;
    }

    /**
     * Postavi grad
     * @param town
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * Dohvati adresu
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * Postavi adresu
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * ohvati broj mobitela
     * @return
     */
    public String getPhone_number() {
        return phone_number;
    }

    /**
     * Postavi broj mobitela
     * @param phone_number
     */
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    /**
     * Dohvati datum rodenja
     * @return
     */
    public String getBirth_date() {
        return birth_date;
    }

    /**
     * Postavi datum rodenja
     * @param birth_date
     */
    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    /**
     * Dohvati IBAN
     * @return
     */
    public String getUser_iban() {
        return user_iban;
    }

    /**
     * Postavi IBAN
     * @param user_iban
     */
    public void setUser_iban(String user_iban) {
        this.user_iban = user_iban;
    }

    /**
     * Dohvati popis ignoriranih listi
     * @return
     */
    public List<String> getIgnoredLists() {
        return ignoredLists;
    }

    /**
     * Postavi popis ignoriranih listi
     * @param ignoredLists
     */
    public void setIgnoredLists(List<String> ignoredLists) {
        this.ignoredLists = ignoredLists;
    }
}
