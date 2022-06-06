package com.hansung.android.myapplication2;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class GeoUtility {
   private Context context;

    GeoUtility(Context context) {
        this.context = context;
    }

    LatLng addressToLatLng(String address) {
        Geocoder coder = new Geocoder(context);

        List<Address> result = null;
        try {
            result = coder.getFromLocationName(address, 1);

            if (result.size() > 0) {
                Address location = result.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
