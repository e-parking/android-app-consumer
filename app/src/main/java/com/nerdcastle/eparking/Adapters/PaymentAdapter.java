package com.nerdcastle.eparking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nerdcastle.eparking.Activities.ReportActivity;
import com.nerdcastle.eparking.PoJoClasses.MapLocation;
import com.nerdcastle.eparking.PoJoClasses.TransactionHistory;
import com.nerdcastle.eparking.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.Viewholder> {

    private List<TransactionHistory> data;
    private TransactionHistory model;
    public static Context context;
    public LatLng mMapLocation = new LatLng(23.8370932,90.3747912);

    public PaymentAdapter(List<TransactionHistory> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public static class Viewholder extends RecyclerView.ViewHolder implements OnMapReadyCallback {

        // 1. Declare your Views here

        public TextView mTransactionID;
        public TextView mTransactionAmount;
        public TextView mTransactionDate;
        public ImageView mVehicleType;
        public TextView mVehicleNumber;
        public MapView mapImageView;
        public TextView mAddress;
        public de.hdodenhof.circleimageview.CircleImageView circularImageView;
        public TextView mUserName;
        public TextView mReportIssue;

        protected GoogleMap mGoogleMap;
        protected MapLocation mMapLocation = new MapLocation();




        public Viewholder(View itemView) {
            super(itemView);

            // 2. Define your Views here

            mTransactionID = (TextView)itemView.findViewById(R.id.mTransactionID);
            mTransactionAmount = (TextView)itemView.findViewById(R.id.mTransactionAmount);
            mTransactionDate = (TextView)itemView.findViewById(R.id.mTransactionDate);
            mVehicleType = (ImageView)itemView.findViewById(R.id.mVehicleType);
            mVehicleNumber = (TextView)itemView.findViewById(R.id.mVehicleNumber);
            mapImageView = (com.google.android.gms.maps.MapView)itemView.findViewById(R.id.mapImageView);
            mAddress = (TextView)itemView.findViewById(R.id.mAddress);
            circularImageView = (de.hdodenhof.circleimageview.CircleImageView)itemView.findViewById(R.id.circularImageView);
            mUserName = (TextView)itemView.findViewById(R.id.mUserName);
            mReportIssue = (TextView)itemView.findViewById(R.id.mReportIssue);

            mapImageView.onCreate(null);
            mapImageView.getMapAsync(this);


            mReportIssue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ReportActivity.class));
                   /* Toast.makeText(context, "Method not ready Yet.", Toast.LENGTH_SHORT).show();*/
                }
            });

        }



        public void setMapLocation(MapLocation mapLocation) {
            mMapLocation = mapLocation;

            // If the map is ready, update its content.
            if (mGoogleMap != null) {
                updateMapContents();
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            MapsInitializer.initialize(context);
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            // If we have map data, update the map content.
            if (mMapLocation != null) {
                updateMapContents();
            }
        }

        protected void updateMapContents() {
            // Since the mapView is re-used, need to remove pre-existing mapView features.
            mGoogleMap.clear();

            // Update the mapView feature data and camera position.
            mGoogleMap.addMarker(new MarkerOptions().position(mMapLocation.center));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMapLocation.center, 15f);
            mGoogleMap.moveCamera(cameraUpdate);
        }



    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_transaction, parent, false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {

        model = data.get(position);

        holder.mTransactionID.setText("TransactionID: "+model.getmTransactionID().toUpperCase());
        holder.mTransactionAmount.setText("৳"+model.getmTransactionAmount());
        holder.mTransactionDate.setText(milisToDate(model.getmTransactionCreated()));
        holder.mVehicleNumber.setText(model.getmVehicleNumber());
        holder.mAddress.setText(model.getmTransactionAddress());
        holder.mUserName.setText(model.getmProviderName());
        holder.mVehicleType.setImageResource(R.drawable.ic_vehicle_car);
        String mLat = model.getmLatitude();
        String mLong = model.getmLogitude();
        System.out.println(mLat+"<<<<<<<<>>>>>>>>>>>"+mLong);
        Double latitude = Double.parseDouble(mLat);
        Double longitude = Double.parseDouble(mLong);

        holder.setMapLocation(new MapLocation("Dhaka",latitude,longitude));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public String milisToDate(String millis)
    {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  "+millis);
        long currentDateMillis = Long.parseLong(millis);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(currentDateMillis);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MMM-dd-yyyy h:mm:ss a");
        dateFormat1.setTimeZone(calendar1.getTimeZone());
        return dateFormat1.format(calendar1.getTime());
    }

}
