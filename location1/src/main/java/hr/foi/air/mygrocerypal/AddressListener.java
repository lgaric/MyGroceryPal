package hr.foi.air.mygrocerypal;

public interface AddressListener {
    public void addressReceived(String fullAddress);
    public void addressNotReceived(String errorMessage);
}
