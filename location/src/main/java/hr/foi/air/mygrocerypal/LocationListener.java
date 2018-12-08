package hr.foi.air.mygrocerypal;

import android.location.Location;

public interface LocationListener {
    public Location locationReceived(Location location);
    public String dataNotReceived(String errorMessage);
}
