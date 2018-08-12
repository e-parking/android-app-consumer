package com.nerdcastle.eparking.PoJoClasses;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Mobile App Develop on 5/22/2018.
 */

public class MyItems implements ClusterItem {
    private LatLng latLng;
    private String title;
    private String snippet;
    private BitmapDescriptor icon;

    public MyItems(LatLng latLng, String title, String snippet) {
        this.latLng = latLng;
        this.title = title;
        this.snippet = snippet;
    }


    public MyItems(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }

}
