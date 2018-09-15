package com.nerdcastle.eparking.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nerdcastle.eparking.Activities.PaymentActivity;
import com.nerdcastle.eparking.PoJoClasses.ParkingRequest;
import com.nerdcastle.eparking.PoJoClasses.Status;
import com.nerdcastle.eparking.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.Viewholder> {

    public static FirebaseDatabase mFirebaseInstance;
    public static FirebaseAuth auth;
    public static ProgressDialog progressDialog;
    public static List<ParkingRequest> requestList;
    public static Context context;

    public static String mProviderID;

    public ActivityAdapter(List<ParkingRequest> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
        mFirebaseInstance = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mProviderID = auth.getCurrentUser().getUid();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {


        public ImageView mParkImage;
        public TextView mParkAddress;
        public TextView mStatus;
        public TextView mDurationTV,phoneNumberTV;
        public Button mEndButton,callButton,mDirection;


        public Viewholder(final View itemView) {
            super(itemView);

            // 2. Define your Views here

            mParkImage = (ImageView)itemView.findViewById(R.id.parkPlaceImage);
            mParkAddress = (TextView)itemView.findViewById(R.id.addressTV);
            mStatus = (TextView)itemView.findViewById(R.id.status_id);
            mDurationTV = (TextView)itemView.findViewById(R.id.durationTV);
            mEndButton = (Button)itemView.findViewById(R.id.endButtonId);
            phoneNumberTV=(TextView)itemView.findViewById(R.id.phoneNumberTV);
            callButton=(Button)itemView.findViewById(R.id.callButton);
            mDirection=(Button)itemView.findViewById(R.id.mapButton);


        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_activities, parent, false);
        return new Viewholder(itemView);

    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {

        final ParkingRequest model = requestList.get(position);
        holder.mParkAddress.setText(model.getmParkPlaceAddress());
        Picasso.get().load(model.getmParkPlacePhotoUrl())
                .placeholder(context.getResources().getDrawable(R.drawable.header_cover))
                .error(context.getResources().getDrawable(R.drawable.header_cover))
                .into(holder.mParkImage);


        final long startTime=model.getmStartTime();
        final String number=model.getmProviderPhone();
        holder.phoneNumberTV.setText(number);

        if (model.getmStatus().equals(Status.STARTED))
        {
            if (startTime!=0){
                holder.mDurationTV.setVisibility(View.VISIBLE);
                long currentTime=System.currentTimeMillis();
                long timeDistance=currentTime-startTime;
                int hour= (int) (timeDistance/3600000);
                timeDistance=timeDistance-(hour*3600000);
                int min= (int) (timeDistance/60000);
                timeDistance=timeDistance-(min*60000);
                int sec= (int) (timeDistance/1000);
                holder.mDurationTV.setText(hour+"h:"+min+"m:"+sec+"s ago");
            }
        }
        else if (model.getmStatus().equals(Status.ENDED))
        {
            holder.mDurationTV.setVisibility(View.VISIBLE);
            long endTime=model.getmEndTime();
            long timeDistance=endTime-startTime;
            int hour= (int) (timeDistance/3600000);
            timeDistance=timeDistance-(hour*3600000);
            int min= (int) (timeDistance/60000);
            timeDistance=timeDistance-(min*60000);
            int sec= (int) (timeDistance/1000);
            holder.mDurationTV.setText("Total- "+hour+"h:"+min+"m:"+sec+"s");
        }





        String mStatus=model.getmStatus();
        if (mStatus.equals(Status.STARTED))
        {
            holder.mStatus.setText(Status.STARTED);
            holder.mEndButton.setVisibility(View.VISIBLE);
        }
        else if (mStatus.equals(Status.PENDING)){
            holder.mStatus.setText(Status.PENDING);
            holder.mEndButton.setVisibility(View.GONE);

        }
        else if (mStatus.equals(Status.REJECTED)){
            holder.mStatus.setText(Status.REJECTED);
            holder.mEndButton.setVisibility(View.GONE);
        }
        else if (mStatus.equals(Status.ENDED)){
            holder.mStatus.setText(Status.ENDED);
            holder.mEndButton.setVisibility(View.GONE);
        }
        else if (mStatus.equals(Status.ACCEPTED)){
            holder.mStatus.setText(Status.ACCEPTED);
            holder.mEndButton.setVisibility(View.GONE);
        }



        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "You are calling "+number, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 456);
                    return;
                }
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        // 3. set the requestList to your Views here


        holder.mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference parkPlaceRequestDB=mFirebaseInstance.getReference("ProviderList/"+mProviderID+"/ParkPlaceList/" + model.getmParkPlaceID()+"/Request/"+model.getmRequestID());
                DatabaseReference consumerRequestDB=mFirebaseInstance.getReference("ConsumerList/"+model.getmConsumerID()+"/Request/"+model.getmRequestID());
                DatabaseReference parkPlaceDB=mFirebaseInstance.getReference("ProviderList/"+mProviderID+"/ParkPlaceList/" + model.getmParkPlaceID());

                parkPlaceRequestDB.child("mStatus").setValue(Status.ENDED, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(context, Status.ENDED+" service", Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(context, PaymentActivity.class);
                        intent.putExtra("RequestId",model.getmRequestID());
                        intent.putExtra("mProviderId",model.getmProviderID());
                        context.startActivity(intent);
                    }
                });
                consumerRequestDB.child("mStatus").setValue(Status.ENDED);
                consumerRequestDB.child("mEndTime").setValue(System.currentTimeMillis());
                parkPlaceRequestDB.child("mEndTime").setValue(System.currentTimeMillis());
                parkPlaceDB.child("mIsAvailable").setValue("true");

                holder.mEndButton.setText(Status.ENDED);
                holder.mEndButton.setEnabled(false);

            }
        });






    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }



}