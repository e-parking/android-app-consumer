package com.nerdcastle.eparking.OtherClasses;

public class TempData {
    public static Double latitude;
    public static Double longitude;

    public static Double getLatitude() {
        if (latitude!=null)
            return latitude;
        else
            return 23.7870804;
    }

    public static void setLatitude(Double latitude) {
        TempData.latitude = latitude;
    }

    public static Double getLongitude() {
        if (longitude!=null)
            return longitude;
        else
            return 90.4231148;
    }

    public static void setLongitude(Double longitude) {
        TempData.longitude = longitude;
    }
}
