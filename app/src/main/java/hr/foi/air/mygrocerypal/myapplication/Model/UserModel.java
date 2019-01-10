package hr.foi.air.mygrocerypal.myapplication.Model;


import java.util.List;

public class UserModel {
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

    public UserModel(){}

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

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getUser_iban() {
        return user_iban;
    }

    public void setUser_iban(String user_iban) {
        this.user_iban = user_iban;
    }

    public List<String> getIgnoredLists() {
        return ignoredLists;
    }

    public void setIgnoredLists(List<String> ignoredLists) {
        this.ignoredLists = ignoredLists;
    }
}
