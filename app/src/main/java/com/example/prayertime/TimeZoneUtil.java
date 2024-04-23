package com.example.prayertime;

import java.util.TimeZone;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;


public class TimeZoneUtil {
    /*
}
    public static String getTimeZone( double latitude, double longitude) {

                return getTimeZoneId(latitude,longitude);



    }

    private static String getTimeZoneId(double latitude, double longitude) {
        return TimeZone.getTimeZone(String.format("%f,%f", latitude, longitude)).getID();
    }
    */
    public static String getTimeZone() {
        // Get the default timezone ID from the system
        return TimeZone.getDefault().getID();
    }
}
