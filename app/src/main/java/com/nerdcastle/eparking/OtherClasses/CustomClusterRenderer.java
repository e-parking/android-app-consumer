package com.nerdcastle.eparking.OtherClasses;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.nerdcastle.eparking.PoJoClasses.MyItems;
import com.nerdcastle.eparking.R;


/**
 * Created by User on 11/30/2016.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<MyItems> {
    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItems> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItems item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.parking_space_icon);

        markerOptions.icon(markerDescriptor);
    }
}
