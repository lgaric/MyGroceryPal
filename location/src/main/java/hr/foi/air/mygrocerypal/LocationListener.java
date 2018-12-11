package hr.foi.air.mygrocerypal;

import android.location.Location;

public interface LocationListener {
     void locationReceived(Location location);
     void dataNotReceived(String errorMessage);
}
