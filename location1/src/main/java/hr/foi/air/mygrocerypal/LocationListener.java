package hr.foi.air.mygrocerypal;

import android.location.Location;

public interface LocationListener {
    public void locationReceived(Location location);
    public void locationNotReceived(String errorMessage);
}
