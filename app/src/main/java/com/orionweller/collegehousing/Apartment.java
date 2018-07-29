package com.orionweller.collegehousing;

import android.location.Location;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class Apartment implements Serializable {
    // TODO implement a class t hold stuff
    public String name;
    public Integer price;
    public String address;
    public String longitude;
    public String latitude;
    public double distance;
    double metersToMilesConvert = 0.000621371;


    Apartment(String mName, Integer mPrice, String mAddress, String mLatitude, String mLongitude) {
        name = mName;
        price = mPrice;
        address = mAddress;
        latitude = mLatitude;
        longitude = mLongitude;

        // get location and distance info
        Location BYU = new Location("");
        BYU.setLatitude(40.2518);
        BYU.setLongitude(-111.6493);

        Location currentLocation = new Location("");
        currentLocation.setLatitude(Double.parseDouble(latitude));
        currentLocation.setLongitude(Double.parseDouble(longitude));

        distance = currentLocation.distanceTo(BYU) * metersToMilesConvert;

    }

    Apartment(String mName, String mAddress, String mLatitude, String mLongitude) {
        name = mName;
        address = mAddress;
        latitude = mLatitude;
        longitude = mLongitude;

        // get location and distance info
        Location BYU = new Location("");
        BYU.setLatitude(40.2518);
        BYU.setLongitude(-111.6493);

        Location currentLocation = new Location("");
        currentLocation.setLatitude(Double.parseDouble(latitude));
        currentLocation.setLongitude(Double.parseDouble(longitude));

        distance = currentLocation.distanceTo(BYU) * metersToMilesConvert;

    }

    Apartment(String mName, int mPrice, String mLatitude, String mLongitude, double mDistance) {
        name = mName;
        price = mPrice;
        distance = mDistance;
        latitude = mLatitude;
        longitude = mLongitude;

    }

    Apartment(){
        // do nothing
    }

    @Override
    public boolean equals(Object obj) {
        // self check
        if (this == obj)
            return true;
        // null check
        if (obj == null)
            return false;
        // type check and cast
        if (getClass() != obj.getClass())
            return false;
       Apartment apt = (Apartment) obj;
        // field comparison
        return this.name == apt.name;
    }
}
